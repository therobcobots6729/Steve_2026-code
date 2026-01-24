// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TestIntake extends SubsystemBase {
  public static TalonSRX intakeMotor;
  public BooleanSupplier a;
  public BooleanSupplier b;
  /** Creates a new TestIntake. */
  public TestIntake() {
    intakeMotor = new TalonSRX(30);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
