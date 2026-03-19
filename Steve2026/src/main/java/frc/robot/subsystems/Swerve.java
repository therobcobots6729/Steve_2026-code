package frc.robot.subsystems;



import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.SwerveModule;

public class Swerve extends SubsystemBase {
  public SwerveDriveOdometry swerveOdometry;
  public SwerveModule[] mSwerveMods;
  // public static AHRS gyro;
  public Pigeon2 gyro;
  public RobotConfig Rconfig;

  
  public Swerve() {
    
  
    
    
       // Handle exception as needed
       
       
    gyro = new Pigeon2(Constants.Swerve.pigeonID);
    gyro.getConfigurator().apply(new Pigeon2Configuration());
    gyro.setYaw(0);
    
     mSwerveMods =
        new SwerveModule[] {
          new SwerveModule(0, Constants.Swerve.Mod0.constants),
          new SwerveModule(1, Constants.Swerve.Mod1.constants),
          new SwerveModule(2, Constants.Swerve.Mod2.constants),
          new SwerveModule(3, Constants.Swerve.Mod3.constants)
        };
        
    swerveOdometry =
        new SwerveDriveOdometry(
            Constants.Swerve.swerveKinematics, getGyroYaw(), getModulePositions());

    try{
      Rconfig = RobotConfig.fromGUISettings();
    } catch (Exception e) {
      // Handle exception as needed
      e.printStackTrace();
    }

  
  
  AutoBuilder.configure(
        this::getPose, // Robot pose supplier
        this::setPose, // Method to reset odometry (will be called if your auto has a starting pose)
        this::getCurrentSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
        this::driveRobotRelativeAuto, // Method that will drive the robot given ROBOT RELATIVE
        // ChassisSpeeds
        
        new PPHolonomicDriveController( // HolonomicPathFollowerConfig, this should likely live in
            // your
            // Constants class
            new PIDConstants(8, 0.0, 0.0), // Translation PID constants
            new PIDConstants(20, 0.0, 0.0) // Rotation PID constants
            // Drive base radius in meters. Distance from robot center to furthest module.
             // Default path replanning config. See the API for the options
            // here
            ),
            Rconfig,
        () -> {
          // Boolean supplier that controls when the path will be mirrored for the red
          // alliance
          // This will flip the path being followed to the red side of the field.
          // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

          var alliance = DriverStation.getAlliance();
          if (alliance.isPresent()) {
            return alliance.get() == DriverStation.Alliance.Red;
          }
          return false;
        },
        
        this // Reference to this subsystem to set requirements
        );
  }
  

  public void drive(
      Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
    SwerveModuleState[] swerveModuleStates =
        Constants.Swerve.swerveKinematics.toSwerveModuleStates(
            fieldRelative
                ? ChassisSpeeds.fromFieldRelativeSpeeds(
                    translation.getX(), translation.getY(), rotation, getHeading())
                // getGyroYaw())
                : new ChassisSpeeds(translation.getX(), translation.getY(), rotation));
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

    for (SwerveModule mod : mSwerveMods) {
      mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
    }
  }
  public ChassisSpeeds getSpeeds() {
    return Constants.Swerve.swerveKinematics.toChassisSpeeds(getModuleStates());
}
public void driveRobotRelativeAuto(ChassisSpeeds desirChassisSpeeds) {
    driveRobotRelative(desirChassisSpeeds, false);
}
public void driveRobotRelative(ChassisSpeeds desiredChassisSpeeds, boolean isOpenLoop) {
        ChassisSpeeds.discretize(desiredChassisSpeeds, 0.02); 
        
        SwerveModuleState[] swerveModuleStates = Constants.Swerve.swerveKinematics.toSwerveModuleStates(desiredChassisSpeeds); 
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

        for(SwerveModule mod : mSwerveMods) {
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
        }
    }


  /* Used by SwerveControllerCommand in Auto */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed);

    for (SwerveModule mod : mSwerveMods) {
      mod.setDesiredState(desiredStates[mod.moduleNumber], false);
    }
  }

  public SwerveModuleState[] getModuleStates() {
    SwerveModuleState[] states = new SwerveModuleState[4];
    for (SwerveModule mod : mSwerveMods) {
      states[mod.moduleNumber] = mod.getState();
    }
    return states;
  }

  public SwerveModulePosition[] getModulePositions() {
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    for (SwerveModule mod : mSwerveMods) {
      positions[mod.moduleNumber] = mod.getPosition();
    }
    return positions;
  }

  public Pose2d getPose() {
    return swerveOdometry.getPoseMeters();
  }

  public void setPose(Pose2d pose) {
    swerveOdometry.resetPosition(getGyroYaw(), getModulePositions(), pose);
  }

  public ChassisSpeeds getCurrentSpeeds() {
    return Constants.Swerve.swerveKinematics.toChassisSpeeds(getModuleStates());
  }

 

  public Rotation2d getHeading() {
    return getPose().getRotation();
  }

  public void setHeading(Rotation2d heading) {
    swerveOdometry.resetPosition(
        getGyroYaw(), getModulePositions(), new Pose2d(getPose().getTranslation(), heading));
  }

  public void zeroHeading() {
    swerveOdometry.resetPosition(
        getGyroYaw(),
        getModulePositions(),
        new Pose2d(getPose().getTranslation(), new Rotation2d()));
  }

  public Rotation2d getGyroYaw() {
    return Rotation2d.fromDegrees(gyro.getYaw().getValueAsDouble()); // here
  }

  public void resetModulesToAbsolute() {
    for (SwerveModule mod : mSwerveMods) {
      mod.resetToAbsolute();
    }
  }
  
  public Translation2d turretVelocity(){
 
    ChassisSpeeds T = getCurrentSpeeds();
    
    double vx = T.vxMetersPerSecond;
    double vy = T.vyMetersPerSecond;
    Rotation2d y= new Rotation2d(Turret.getAngle());
    Translation2d h = new Translation2d(vx,vy).rotateBy(y);

   
    

// rotate it into target frame
    

// add to chassis velocity
    
    return h;
  } 

  @Override
  public void periodic() {
    swerveOdometry.update(getGyroYaw(), getModulePositions());

    for (SwerveModule mod : mSwerveMods) {
      SmartDashboard.putNumber(
          "Mod " + mod.moduleNumber + " CANcoder", mod.getCANcoder().getDegrees());
      SmartDashboard.putNumber(
          "Mod " + mod.moduleNumber + " Angle", mod.getPosition().angle.getDegrees());
      SmartDashboard.putNumber(
          "Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);
          SmartDashboard.putNumber("Yaw", gyro.getYaw().getValueAsDouble());
          SmartDashboard.putNumber("HEADING", getPose().getRotation().getDegrees());
          SmartDashboard.putNumber("Turret heading", turretVelocity().getAngle().getDegrees());

    }
  }
}
