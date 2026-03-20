// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.RunIntake;
import frc.robot.commands.Shooty;
import frc.robot.commands.TeleopSwerve;
import frc.robot.commands.Agitate;
import frc.robot.commands.AutoTurret;
import frc.robot.commands.Extended;
import frc.robot.commands.Look;
import frc.robot.commands.ManTurret;
import frc.robot.commands.Retracted;
import frc.robot.commands.RunIndexer;
import frc.robot.commands.AutoCommands;
import frc.robot.commands.runShooter;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Velocity;
import frc.robot.subsystems.Angle;
import frc.robot.subsystems.Flipper;
import frc.robot.subsystems.Funnel;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.LimelightHelpers;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */


public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  
  private final Intake i_Intake = new Intake();
  private final Funnel funnel = new Funnel();
  private final Limelight Limelight = new Limelight(); 
  private final Swerve s_Swerve = new Swerve();
  private final Velocity velocity = new Velocity(Limelight, s_Swerve);
  private final Angle angle = new Angle(velocity, s_Swerve, Limelight);
  private final Turret turret = new Turret(angle);
  private final Flipper flip = new Flipper();
  private final Shooter shooter = new Shooter(velocity);  
  private final Indexer indexer = new Indexer();
  private final LimelightHelpers ll = new LimelightHelpers();
  //private final AutoCommands autoCommands;
 // private final LoggedDashboardChooser<Command> autoChooser;
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final XboxController driver =
      new XboxController(0);
  private final Joystick operator = new Joystick(1);
      

  
  /* Drive Controls */
  private final int translationAxis = XboxController.Axis.kLeftY.value;
  private final int strafeAxis = XboxController.Axis.kLeftX.value;
  private final int rotationAxis = XboxController.Axis.kRightX.value;


  /* Drive Buttons */
  private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kRightStick.value);
  private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kStart.value);
  private final JoystickButton intakeForward = new JoystickButton(driver, XboxController.Button.kRightBumper.value);
  private final JoystickButton intakeReverse = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
  private final JoystickButton manTurret = new JoystickButton(driver, XboxController.Button.kY.value);  
  private final JoystickButton shoot = new JoystickButton(driver, XboxController.Button.kX.value); 
  private final JoystickButton raise = new JoystickButton(driver, XboxController.Button.kA.value);
  private final JoystickButton lower = new JoystickButton(driver, XboxController.Button.kB.value);
  private final POVButton agitate = new POVButton(driver, 0);
  private final Trigger indexUp = new Trigger(() -> driver.getLeftTriggerAxis() > 0.10);
  private final Trigger rightTrigger = new Trigger(() -> driver.getRightTriggerAxis() > .15 );
  
  /* Operator Buttons */
  private final JoystickButton shoot2 = new JoystickButton(operator, 3);
  private final JoystickButton index = new JoystickButton(operator, 2);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    s_Swerve.setDefaultCommand(
        new TeleopSwerve(
            s_Swerve,
            () -> -driver.getRawAxis(translationAxis),
            () -> -driver.getRawAxis(strafeAxis),
            () -> -driver.getRawAxis(rotationAxis),
            () -> robotCentric.getAsBoolean()));

    /*i_Intake.setDefaultCommand(
        new RunIntake(
            i_Intake, 
            () -> driver.getRawAxis(intakeForward2)));*/
    
    turret.setDefaultCommand( //this runs command automatically
      new AutoTurret(turret, ()-> Limelight.hasTarget())
      );
    configureBindings();
   // autoCommands = new AutoCommands( flip, funnel, indexer, shooter, s_Swerve, turret, i_Intake);

        // Set up auto routines
       // autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());
     // autoChooser.addOption("Left", autoCommands.startMid());
    // Another option that allows you to specify the default auto by its name
    // autoChooser = AutoBuilder.buildAutoChooser("My Default Auto");

    
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroHeading()));
    
    intakeForward.whileTrue(new RunIntake(i_Intake));
    indexUp.whileTrue(new RunIndexer(indexer, funnel));
    manTurret.whileTrue(new ManTurret(turret));
    shoot.whileTrue(new Shooty(shooter));
    intakeReverse.onTrue(new Look(turret, () -> Limelight.hasTarget()));
    rightTrigger.whileTrue(new runShooter(shooter));
    lower.onTrue (new Extended(flip));
    raise.onTrue(new Retracted(flip));
    agitate.onTrue(new Agitate(flip));
    index.whileTrue(new RunIndexer(indexer, funnel));
    shoot2.whileTrue(new Shooty(shooter));
    
    

    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
   return null;// autoChooser.get();
  }
}
