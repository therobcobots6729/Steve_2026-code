// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Funnel extends SubsystemBase {
  /** Creates a new Funnel. */
  private TalonSRX right;
  private TalonSRX left;
  private double speed = .5;
  public Funnel() {
    right = new TalonSRX(20);
    left = new TalonSRX(21);

  }
  public void Spin(){
    left.set(ControlMode.PercentOutput, -speed);
    right.set(ControlMode.PercentOutput, speed);
  }
  public void Stop(){
    left.set(ControlMode.PercentOutput, 0);
    right.set(ControlMode.PercentOutput, 0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
