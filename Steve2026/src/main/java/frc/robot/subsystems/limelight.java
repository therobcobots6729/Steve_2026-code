// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



import edu.wpi.first.math.geometry.Pose3d;
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
   double poiZ,poiX,poiY,yaw,distance;
  private static final double TARGET_MEMORY_TIME = 0.6; // seconds

 
  
 
  
  

  private double targetOffsetAngle_Vertical;

  public Limelight() {
    
     limelightMountAngleDegrees = 20;
     limelimelightLensHeightInches = 26.26;
     GoalHeightInches = 56.44;
     table1 = NetworkTableInstance.getDefault().getTable("limelight");
     tx1 = table1.getEntry("tx");
     ty = table1.getEntry("ty");
     tid1 = table1.getEntry("tid");
     tv = table1.getEntry("tv");
    
     
  }
  
  public  boolean hasTarget(){
    return tv.getDouble(0.0) > 0.5;
}
   

public double Yaw(){
  return yaw;
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
public double distance() {
   

    double distanceMeters = Math.sqrt(poiX*poiX + poiZ*poiZ);
    return distanceMeters * 39.3701;}
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
public void updateVision() {
    Pose3d pose = LimelightHelpers.getTargetPose3d_CameraSpace("limelight");

    double x = pose.getX();
    double y = pose.getY();
    double z = pose.getZ();

    poiX = x + 0.0698;
    poiY = y - 0.3048;
    poiZ = z - 0.5842;

    yaw =Math.toDegrees(Math.atan2(poiX, poiZ));
    distance = Math.sqrt(poiX*poiX + poiZ*poiZ);
}
  @Override
  public void periodic() {
      
      targetOffsetAngle_Vertical = ty.getDouble(0.0);
    
      if(hasTarget()){
       
        lastSeenTime = Timer.getFPGATimestamp();
    }
      updateVision();
      

     
     

     
     
     
     
    SmartDashboard.putNumber("tx1", poiX);
    SmartDashboard.putNumber("ty1", poiZ);
    SmartDashboard.putNumber("tid1", tid1.getDouble(0.0));
    SmartDashboard.putNumber("yaw", yaw);
    SmartDashboard.putNumber("distance", distance());
    SmartDashboard.putBoolean("target", hasTarget());
    
    // This method will be called once per scheduler run
  }
}
