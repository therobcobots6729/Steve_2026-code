// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Limelight extends SubsystemBase {
  /** Creates a new limelight. */
  public static double tx;
  private double limelightMountAngleDegrees;
  private double limelimelightLensHeightInches;
  private double GoalHeightInches;
  
  private NetworkTable table1;
  private NetworkTableEntry tx1;
  private NetworkTableEntry ty;
  private NetworkTableEntry tid1;
  private  NetworkTableEntry tv;
  private double lastValidDistance = 0;
  private double lastSeenTime = 0;
  public double targetHeadingDeg = 0;
  private static final double TARGET_MEMORY_TIME = 0.6; // seconds

 
  
 
  
  

  private double targetOffsetAngle_Vertical;

  public Limelight() {
    
     limelightMountAngleDegrees = 45;
     limelimelightLensHeightInches = 26.26;
     GoalHeightInches = 56.44;
     table1 = NetworkTableInstance.getDefault().getTable("limelight");
     tx1 = table1.getEntry("tx");
     ty = table1.getEntry("ty");
     tid1 = table1.getEntry("tid");
     tv = table1.getEntry("tv");
     
  }
  public double distance(){
    double angletoGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
     double angletoGoalRadians= angletoGoalDegrees * (Math.PI/180);
     double distanceFromLimelighttoGoalInches = (GoalHeightInches-limelimelightLensHeightInches)/Math.tan(angletoGoalRadians);
     double d = distanceFromLimelighttoGoalInches;
     return d;
  }
  public  boolean hasTarget(){
    return tv.getDouble(0.0) > 0.5;
}
    private double wrapAngle(double angle){
    while(angle > 180) angle -= 360;
    while(angle < -180) angle += 360;
    return angle;
  }
public void updateTargetHeading()
{double turretAngle = Turret.getAngle();
    if (hasTarget()) {
        targetHeadingDeg = wrapAngle(turretAngle + Limelight.tx);
    }
}

  
  public double distanceMeters(){

    return distance() * 0.0254;   // inches → meters
}
  public double getTX(){
    tx = tx1.getDouble(0.0);
    return tx;
  }
                //create f(distance()) based on tested values,   get an upper and lower limit for each distance. 
                // either use nplot to set the distance and the required speed
                // either use nplot to set the distance and the required speed+-room for error/2 and use the best fit line
                // or send me the values and i will send you back the correct function
                // do not use voltage numbers only velocity numbers

  public double getHeldDistanceMeters(){

    double timeSinceSeen = Timer.getFPGATimestamp() - lastSeenTime;

    // still trust last measurement
    if(timeSinceSeen < TARGET_MEMORY_TIME){
        return lastValidDistance;
    }
    else{
    // target gone too long → stop trusting
    return 0;}
}
  @Override
  public void periodic() {
      
      targetOffsetAngle_Vertical = ty.getDouble(0.0);
    updateTargetHeading();
      if(hasTarget()){
        lastValidDistance = distanceMeters();
        lastSeenTime = Timer.getFPGATimestamp();
    }
      
      

     
     

     
     
     
     
    SmartDashboard.putNumber("tx1", tx1.getDouble(0.0));
    SmartDashboard.putNumber("ty1", ty.getDouble(0.0));
    SmartDashboard.putNumber("tid1", tid1.getDouble(0.0));
    
    SmartDashboard.putNumber("distance", distance());
    SmartDashboard.putBoolean("target", hasTarget());
    
    // This method will be called once per scheduler run
  }
}
