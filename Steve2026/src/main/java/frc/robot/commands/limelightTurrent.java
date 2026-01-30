// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Turrent;
import frc.robot.subsystems.limelight;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class limelightTurrent extends Command {
  /** Creates a new limelightTurrent. */
  private Turrent turrent;
  private limelight Limelight;
  public limelightTurrent(Turrent turrent, limelight Limelight) {
    addRequirements(turrent,Limelight);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Turrent.getAngle()<90 && Turrent.getAngle()>-90  ){Turrent.turnMotor.set(Turrent.controller.calculate(0-limelight.tx));
  }
  else {Turrent.turnMotor.set(Turrent.controller.calculate(0-Turrent.getAngle()));}
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
