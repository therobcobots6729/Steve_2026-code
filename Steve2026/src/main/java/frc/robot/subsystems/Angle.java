// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Add your docs here. */
public class Angle extends SubsystemBase{
    private Velocity velocity;
    private Swerve swerve;
    private Limelight limelight;
    
    public  Angle(Velocity velocity, Swerve swerve, Limelight limelight){
        this.swerve = swerve;
        this.velocity = velocity;
        this.limelight = limelight;
    }


    public double turret_Target(){                                                                                /// angle that the limelight should be pointing relative to actual target
        double t = velocity.flightTime();

    if(t == 0){
        return 0;}
    else{
    double d = limelight.distanceMeters();

    // sideways robot motion
    double vSide = swerve.turretVelocity().getY();

    double lead = vSide * t;

    double theta = Math.toDegrees(Math.atan(lead / d));
    double tx = limelight.getTX();

    return -( tx+theta);}
  }
 

@Override
public void periodic() {
    SmartDashboard.putNumber("theta", turret_Target());
    // This method will be called once per scheduler run
}}
