// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class limelight extends SubsystemBase {
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
  public static double speed;

  public limelight() {
     limelightMountAngleDegrees = 45;
     limelimelightLensHeightInches = 26.26;
     GoalHeightInches = 56.44;
     table1 = NetworkTableInstance.getDefault().getTable("limelight");
     tx1 = table1.getEntry("tx");
     ty = table1.getEntry("ty");
     tid1 = table1.getEntry("tid");
     
  }

  @Override
  public void periodic() {
    tx = tx1.getDouble(0.0);
     double targetOffsetAngle_Vertical = ty.getDouble(0.0);
     
     double angletoGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
     double angletoGoalRadians= angletoGoalDegrees * (3.14159/180);
     double distanceFromLimelighttoGoalInches = (GoalHeightInches-limelimelightLensHeightInches)/Math.tan(angletoGoalRadians);
     d = distanceFromLimelighttoGoalInches;
     speed = d; //create f(d) based on tested values,   get an upper and lower limit for each distance. 
                // either use nplot to set the distance and the required speed
                // either use nplot to set the distance and the required speed+-room for error/2 and use the best fit line
                // or send me the values and i will send you back the correct function
                // do not use voltage numbers only velocity numbers
    SmartDashboard.putNumber("tx1", tx1.getDouble(0.0));
    SmartDashboard.putNumber("ty1", ty.getDouble(0.0));
    SmartDashboard.putNumber("tid1", tid1.getDouble(0.0));

    // This method will be called once per scheduler run
  }
}
