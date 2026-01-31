// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */
  private  TalonFX shooty;
  private  VelocityVoltage shoot;
  private  Slot0Configs pid;

  public Shooter(limelight Limelight) {
    shooty = new TalonFX(32);
    
     pid = new Slot0Configs();
      pid.kP = 0.1; // change this if needed
      pid.kI = 0.0;
      pid.kD = 0.0;
      pid.kV = 0.12; //  (volts per rps) do not change
      pid.kA = 0.01;

    TalonFXConfiguration config = new TalonFXConfiguration();
    config.Slot0 = pid;
    config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    config.CurrentLimits.SupplyCurrentLimit = 40;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;

    shooty.getConfigurator().apply(config);

   shoot = new VelocityVoltage(0);
    
  }
  
  private double speed(){
    double targetRPM = limelight.speed;
    double RPS = targetRPM / 60.0;
    return RPS;
  }
  public void runShooter() {
  shooty.setControl(shoot.withVelocity(speed()));
}


  public void stop() {
  shooty.stopMotor();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("target Shooter velocity", speed()*60);
    SmartDashboard.putNumber("Actual Shooter velocity", shooty.getVelocity().getValueAsDouble());
    // This method will be called once per scheduler run
  }
}
