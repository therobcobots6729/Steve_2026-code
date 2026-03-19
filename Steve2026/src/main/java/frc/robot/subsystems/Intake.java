// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  public TalonFX intakeMotor;
  /** Creates a new TestIntake. */
  public Intake() {
    intakeMotor = new TalonFX(25);

    TalonFXConfiguration config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    config.CurrentLimits.SupplyCurrentLimit = 30;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;

    intakeMotor.getConfigurator().apply(config);

  }
  public void runIntake() {
    intakeMotor.set(0.50);
  }
  public void stopIntake() {
    intakeMotor.set(0);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
