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
  public limelight() {
     
  }

  @Override
  public void periodic() {
    NetworkTable table1 = NetworkTableInstance.getDefault().getTable("limelight");
     NetworkTableEntry tx1 = table1.getEntry("tx");
    NetworkTableEntry ty1 = table1.getEntry("ty");
    NetworkTableEntry tid1 = table1.getEntry("tid");
    tx = tx1.getDouble(0.0);
    SmartDashboard.putNumber("tx1", tx1.getDouble(0.0));
    SmartDashboard.putNumber("ty1", ty1.getDouble(0.0));
    SmartDashboard.putNumber("tid1", tid1.getDouble(0.0));
    // This method will be called once per scheduler run
  }
}
