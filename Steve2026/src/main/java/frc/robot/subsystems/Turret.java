// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.revrobotics.RelativeEncoder;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turret extends SubsystemBase {
  private  SparkMax turnMotor;
  
  private  RelativeEncoder encoder;
  private Limelight limelight;
  private  PIDController controller = new PIDController(0.025, 0, 0);//tune this a little more to stop the shakes
  
  /** Creates a new Turrent. */
  public Turret(Limelight limelight) {
    this.limelight = limelight;
    turnMotor  = new SparkMax(17, MotorType.kBrushless);
    controller.enableContinuousInput(-180, 180);
    controller.setTolerance(1.0);
    encoder = turnMotor.getEncoder();
      
  }

  public double getAngle(){
     double targetAngle = 360*encoder.getPosition()/90;
     return MathUtil.inputModulus(-targetAngle, -180, 180);
  }

  public void RunTurrent(){
     double current = getAngle();
     double target = limelight.turret_Target();
     target = MathUtil.clamp(target, -90, 90);
     double output = controller.calculate(current,target);
     if(controller.atSetpoint()){
      turnMotor.set(0);
     }
     else{
     turnMotor.set(output);
     }
  }

  


  @Override
  public void periodic() {
    SmartDashboard.putNumber("Turrent Angle", getAngle());
    SmartDashboard.putNumber("Tmotor", encoder.getPosition());
    // This method will be called once per scheduler run
  }
}
