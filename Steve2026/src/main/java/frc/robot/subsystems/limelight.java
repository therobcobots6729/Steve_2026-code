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
  private NetworkTableEntry tv;
  private double lastValidDistance = 0;
  private double lastSeenTime = 0;
  private static final double TARGET_MEMORY_TIME = 0.6; // seconds

  private Swerve swerve;
  private Turret turret;
 private double cachedFlightTime;
  public double targetHeadingDeg = 0;
  

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
  public boolean hasTarget(){
    return tv.getDouble(0.0) > 0.5;
}
  private double wrapAngle(double angle){
    while(angle > 180) angle -= 360;
    while(angle < -180) angle += 360;
    return angle;
}
public void updateTargetHeading()
{
    if (hasTarget()) {
        targetHeadingDeg = wrapAngle(turret.getAngle() + tx);
    }
}

  private double distance(){
    double angletoGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
     double angletoGoalRadians= angletoGoalDegrees * (3.14159/180);
     double distanceFromLimelighttoGoalInches = (GoalHeightInches-limelimelightLensHeightInches)/Math.tan(angletoGoalRadians);
     double d = distanceFromLimelighttoGoalInches;
     return d;
  }
  public double distanceMeters(){

    return distance() * 0.0254;   // inches → meters
}

                //create f(distance()) based on tested values,   get an upper and lower limit for each distance. 
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

   
    double vEffective = vShot + vToward;

    
    if (vEffective < 0.5){
        return 0;}
    else{
    return d / vEffective;}
  }
  public double turret_Target(){
        double t = cachedFlightTime;

    if(t == 0){
        return 0;}
    else{
    double d = distanceMeters();

    // sideways robot motion
    double vSide = swerve.turretVelocity().getY();

    double lead = vSide * t;

    double theta = Math.toDegrees(Math.atan(lead / d));

    return -(theta);}
  }
  public double distanceTarget(){
    double t = cachedFlightTime;

    if (t == 0) {// prevents divide by zero 
        return distanceMeters();}

    else{

    double effectiveDistance = velocity() * flightTime();

    return effectiveDistance;}

  }
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
      tx = tx1.getDouble(0.0);
      targetOffsetAngle_Vertical = ty.getDouble(0.0);
    
      if(hasTarget()){
        lastValidDistance = distanceMeters();
        lastSeenTime = Timer.getFPGATimestamp();
    }
      cachedFlightTime = flightTime();
      updateTargetHeading();

     
     

     
     
     
     
    SmartDashboard.putNumber("tx1", tx1.getDouble(0.0));
    SmartDashboard.putNumber("ty1", ty.getDouble(0.0));
    SmartDashboard.putNumber("tid1", tid1.getDouble(0.0));
    SmartDashboard.putNumber("target Shooter velocity", speed()*60);
    SmartDashboard.putNumber("distance", distance());
    SmartDashboard.putBoolean("target", hasTarget());
    
    // This method will be called once per scheduler run
  }
}
