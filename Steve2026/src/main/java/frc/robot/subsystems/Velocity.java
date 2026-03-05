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
    private double x=40;
    private double y = .0195588;
    private double z = .0005359;
    
    
    private double cachedMuzzleVelocity = 0.0; // m/s

    public Velocity(Limelight limelight, Swerve swerve){
        this.limelight = limelight;
        this.swerve = swerve;
        
    }


   public double inputSpeed() {  
    // placeholder ballistic model (distance → RPM)
    double targetRPM = (z*(Math.pow(limelight.distance(),2)))+y*limelight.distance()+x; // future function
    return targetRPM ; // RPS
}


private double velocity() {
    double releaseAngle = 70;
    double releaseAngleRadians = Math.toRadians(releaseAngle);

    // wheel linear speed → projectile speed
    double wheelRadius = 2; // inches
    double wheelLinearSpeed = inputSpeed() * 2 * Math.PI * wheelRadius; // m/s

    // horizontal projectile velocity
    return wheelLinearSpeed * Math.cos(releaseAngleRadians);
}

public double flightTime() {
    double d = limelight.distance();
        return 0;
   

    // using horizontal component for time-of-flight
    //return d / cachedMuzzleVelocity;
}

public double effectiveDistance() {  
    double d = limelight.distance();
    double vx = swerve.turretVelocity().getX()*39.37; // robot forward velocity (in/s)
    double t = flightTime();

    // motion compensation
    return d - (vx * t);
}

public double outputSpeed(){
    // corrected ballistic calculation
    double targetRPM = (z*(Math.pow(effectiveDistance(),2)))+y*effectiveDistance()+x;; // future function
    return targetRPM ; // RPS
}
public boolean inRange(){
    if (outputSpeed()<= 105){
        return true;
    }
    else {return false;}
}
@Override
public void periodic() {
    SmartDashboard.putNumber("target Shooter velocity", outputSpeed());
    cachedMuzzleVelocity = velocity(); // horizontal velocity cache
}
}