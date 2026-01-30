// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.encoder.DetachedEncoder.Model;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turrent extends SubsystemBase {
  public static SparkMax turnMotor;
  public static SparkBase sparkBase;
  public static RelativeEncoder encoder;
  public static PIDController controller = new PIDController(0.025, 0, 0);
  
  /** Creates a new Turrent. */
  public Turrent() {
    turnMotor  = new SparkMax(17, MotorType.kBrushless);
    encoder = turnMotor.getEncoder();
      
  }

  public static double getAngle(){
     double targetAngle = 360*encoder.getPosition()/90;
     return -targetAngle % 360;
  }

/*************  ✨ Windsurf Command ⭐  *************/
/*******  527f2b73-454f-4f1d-9af1-de791d175c7a  *******/
  @Override
  public void periodic() {
    SmartDashboard.putNumber("Turrent Angle", getAngle());
    SmartDashboard.putNumber("Tmotor", encoder.getPosition());
    // This method will be called once per scheduler run
  }
}
