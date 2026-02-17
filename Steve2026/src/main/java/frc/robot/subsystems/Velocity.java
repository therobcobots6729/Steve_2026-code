// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Add your docs here. */
public class Velocity extends SubsystemBase {
    
    private Limelight limelight;
    private Swerve swerve;
    
    private double cachedMuzzleVelocity = 0.0; // m/s

    public Velocity(Limelight limelight, Swerve swerve){
        this.limelight = limelight;
        this.swerve = swerve;
        
    }


   public double inputSpeed() {  
    // placeholder ballistic model (distance → RPM)
    double targetRPM = limelight.distanceMeters(); // future function
    return targetRPM / 60.0; // RPS
}

private double velocity() {
    double releaseAngle = 70;
    double releaseAngleRadians = Math.toRadians(releaseAngle);

    // wheel linear speed → projectile speed
    double wheelRadius = 0.0508; // meters
    double wheelLinearSpeed = inputSpeed() * 2 * Math.PI * wheelRadius; // m/s

    // horizontal projectile velocity
    return wheelLinearSpeed * Math.cos(releaseAngleRadians);
}

public double flightTime() {
    double d = limelight.distanceMeters();

    if (cachedMuzzleVelocity < 0.1) {
        return 0.0;
    }

    // using horizontal component for time-of-flight
    return d / cachedMuzzleVelocity;
}

public double effectiveDistance() {  
    double d = limelight.distanceMeters();
    double vx = swerve.turretVelocity().getX(); // robot forward velocity (m/s)
    double t = flightTime();

    // motion compensation
    return d - (vx * t);
}

public double outputSpeed(){
    // corrected ballistic calculation
    double targetRPM = effectiveDistance(); // future function
    return targetRPM / 60.0; // RPS
}
public boolean inRange(){
    if (outputSpeed()<= 105){
        return true;
    }
    else {return false;}
}
@Override
public void periodic() {
    SmartDashboard.putNumber("target Shooter velocity", outputSpeed()*60);
    cachedMuzzleVelocity = velocity(); // horizontal velocity cache
}
}