// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.RunIntake;
import frc.robot.commands.Shooty;
import frc.robot.commands.TeleopSwerve;
import frc.robot.commands.AutoTurret;
import frc.robot.commands.Look;
import frc.robot.commands.ManTurret;
import frc.robot.commands.RunIndexer;
import frc.robot.commands.runShooter;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.TestIntake;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Velocity;
import frc.robot.subsystems.Angle;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Limelight;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */


public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  
  private final TestIntake i_Intake = new TestIntake();
  
  private final Limelight Limelight = new Limelight(); 
  private final Swerve s_Swerve = new Swerve(Limelight);
  private final Velocity velocity = new Velocity(Limelight, s_Swerve);
  private final Angle angle = new Angle(velocity, s_Swerve, Limelight);
  private final Turret turret = new Turret(angle, Limelight);
  
  private final Shooter shooter = new Shooter(velocity);  
  private final Indexer indexer = new Indexer(shooter);

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final XboxController driver =
      new XboxController(0);

  
  /* Drive Controls */
  private final int translationAxis = XboxController.Axis.kLeftY.value;
  private final int strafeAxis = XboxController.Axis.kLeftX.value;
  private final int rotationAxis = XboxController.Axis.kRightX.value;
  Trigger rightTrigger = new Trigger(() -> driver.getRightTriggerAxis() > .15 );


  /* Drive Buttons */
  private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kRightStick.value);
  private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kStart.value);
  private final JoystickButton intakeForward = new JoystickButton(driver, XboxController.Button.kRightBumper.value);
  private final JoystickButton intakeReverse = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);
    private final JoystickButton manTurret = new JoystickButton(driver, XboxController.Button.kY.value);  
    private final JoystickButton shoot = new JoystickButton(driver, XboxController.Button.kX.value);  
  


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
    
    turret.setDefaultCommand( //this runs command automatically
      new AutoTurret(turret));
    configureBindings();
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
    
    intakeForward.whileTrue(new ParallelCommandGroup(new RunIntake(i_Intake), new RunIndexer(indexer)));
    manTurret.whileTrue(new ManTurret(turret));
    shoot.whileTrue(new Shooty(shooter));
    intakeReverse.onTrue(new Look(turret, Limelight));
    

    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}
