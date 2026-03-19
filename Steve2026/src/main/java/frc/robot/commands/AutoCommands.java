// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.lib.util.PathPlannerUtils;
import frc.robot.subsystems.Flipper;
import frc.robot.subsystems.Funnel;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Turret;
import frc.robot.commands.RunIntake;
import java.util.Optional;
/** Add your docs here. */
public class AutoCommands{
    private final Flipper flippy;
    private final Funnel funnel;
    private final Indexer index;
    private final Intake intake;
    private final Shooter shoot;
    private final Swerve drive;
    private final Turret turret;
    
    public AutoCommands(Flipper flippy, Funnel funnel,Indexer index,
    Shooter shoot, Swerve drive, Turret turret,Intake intake){
        this.flippy = flippy;
        this.drive = drive;
        this.funnel = funnel;
        this.index = index;
        this.intake = intake;
        this.shoot = shoot;
        this.turret = turret;
       
    }

    
    public Command cycleDepot(Optional<PathPlannerPath> pathOne,Optional<PathPlannerPath> pathTwo){
        return Commands.sequence(
            AutoBuilder.followPath(pathOne.get())
            .alongWith((Commands.waitSeconds(4)).andThen(new Extended(flippy).andThen(new RunIntake(intake).withTimeout(2)))),
            AutoBuilder.followPath(pathTwo.get()),
            new runShooter(shoot)
        );
    }
}
