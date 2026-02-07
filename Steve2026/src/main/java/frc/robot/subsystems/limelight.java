// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Limelight extends SubsystemBase {
  /** Creates a new limelight. */
  public static double tx;
  public static double limelightMountAngleDegrees;
  public static double limelimelightLensHeightInches;
  public static double GoalHeightInches;
  public static double d;
  public static NetworkTable table1;
  public static NetworkTableEntry tx1;
  public static NetworkTableEntry ty;
  public static NetworkTableEntry tid1;
  private Swerve swerve;
 private double cachedFlightTime;

  

  private double targetOffsetAngle_Vertical;

  public Limelight() {
     limelightMountAngleDegrees = 45;
     limelimelightLensHeightInches = 26.26;
     GoalHeightInches = 56.44;
     table1 = NetworkTableInstance.getDefault().getTable("limelight");
     tx1 = table1.getEntry("tx");
     ty = table1.getEntry("ty");
     tid1 = table1.getEntry("tid");
     
  }
  private double distance(){
    double angletoGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
     double angletoGoalRadians= angletoGoalDegrees * (3.14159/180);
     double distanceFromLimelighttoGoalInches = (GoalHeightInches-limelimelightLensHeightInches)/Math.tan(angletoGoalRadians);
     double d = distanceFromLimelighttoGoalInches;
     return d;
  }
  private double distanceMeters(){

    return distance() * 0.0254;   // inches → meters
}

                //create f(d) based on tested values,   get an upper and lower limit for each distance. 
                // either use nplot to set the distance and the required speed
                // either use nplot to set the distance and the required speed+-room for error/2 and use the best fit line
                // or send me the values and i will send you back the correct function
                // do not use voltage numbers only velocity numbers
  public double speed(){
    double speed = distanceMeters();
    double targetRPM = speed;
    double RPS = targetRPM / 60.0;
    return RPS;
  }
 
  private double velocity(){
    double releaseAngle = 70;
    double releaseAngleRadians = Math.toRadians(releaseAngle);
    double V = speed()*2*Math.PI*.0508*Math.cos(releaseAngleRadians);
    return V;
  }
   private double flightTime(){
     double d = distanceMeters();

    double vShot = velocity();

    
    double vToward = swerve.turretVelocity().getX();

   
    double vEffective = vShot - vToward;

    
    if (vEffective < 0.5)
        return 0;

    return d / vEffective;
  }
  public double turret_Target(){
        double t = cachedFlightTime;

    if(t == 0)
        return tx;

    double d = distanceMeters();

    // sideways robot motion
    double vSide = swerve.turretVelocity().getY();

    double lead = vSide * t;

    double theta = Math.toDegrees(Math.atan(lead / d));

    return -(tx - theta);
  }
  public double distanceTarget(){
    double t = cachedFlightTime;

    if (t == 0) // prevents divide by zero 
        return distanceMeters();

    

    double effectiveDistance = velocity() * flightTime();

    return effectiveDistance;

  }
  @Override
  public void periodic() {
      tx = tx1.getDouble(0.0);
      targetOffsetAngle_Vertical = ty.getDouble(0.0);
      cachedFlightTime = flightTime();

     
     

     
     
     
     
    SmartDashboard.putNumber("tx1", tx1.getDouble(0.0));
    SmartDashboard.putNumber("ty1", ty.getDouble(0.0));
    SmartDashboard.putNumber("tid1", tid1.getDouble(0.0));
    SmartDashboard.putNumber("target Shooter velocity", speed()*60);
    SmartDashboard.putNumber("distance", distance());

    // This method will be called once per scheduler run
  }
}
