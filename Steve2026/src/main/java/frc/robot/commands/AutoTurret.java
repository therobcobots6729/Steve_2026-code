// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Angle;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Turret;



/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoTurret extends Command {
  /** Creates a new limelightTurrent. */
  private Turret turret;
  private Angle angle;
  private Limelight limelight;
  public AutoTurret(Turret turret, Limelight limelight, Angle angle) {
    this.turret = turret;
    this.limelight = limelight;
    this.angle = angle;
    addRequirements(turret, limelight);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Turret.getAngle()<90 && Turret.getAngle()>-90){
      if (limelight.hasTarget()){
        turret.runTurrent();}
      else if (!limelight.hasTarget()){
         turret.stop();
    }}
    else if (Turret.getAngle()>90 ){
        if (limelight.hasTarget() && angle.turret_Target()<0){
        turret.runTurrent();}
        else if (limelight.hasTarget() && angle.turret_Target()>0){
          turret.stop();
        }
    }
    else if ( Turret.getAngle()<-90){
      if (limelight.hasTarget() && angle.turret_Target()>0){
        turret.runTurrent();}
        else if (limelight.hasTarget() && angle.turret_Target()<0){
          turret.stop();
        }
    }
  }
   

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !limelight.hasTarget();
  }
}
