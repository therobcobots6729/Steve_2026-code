// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.revrobotics.RelativeEncoder;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turret extends SubsystemBase {
  private  SparkMax turnMotor;
  
  private  RelativeEncoder encoder;
  private  PIDController controller = new PIDController(0.025, 0, 0);//tune this a little more to stop the shakes
  
  /** Creates a new Turrent. */
  public Turret(limelight Limelight) {
    turnMotor  = new SparkMax(17, MotorType.kBrushless);
    
    encoder = turnMotor.getEncoder();
      
  }

  private double getAngle(){
     double targetAngle = 360*encoder.getPosition()/90;
     return -targetAngle % 360;
  }

  public void RunTurrent(){
     if (getAngle()<90 && getAngle()>-90  ){turnMotor.set(controller.calculate(0-limelight.tx));
  }
     else {turnMotor.set(controller.calculate(0-getAngle()));}
  }

  


  @Override
  public void periodic() {
    SmartDashboard.putNumber("Turrent Angle", getAngle());
    SmartDashboard.putNumber("Tmotor", encoder.getPosition());
    // This method will be called once per scheduler run
  }
}
