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

public class Indexer extends SubsystemBase {
  /** Creates a new Indexer. */
  
  private TalonFX front;
  //private Shooter shooter;
  private Slot0Configs pid;
  private VelocityVoltage index;
  private double targetSpeed=53;//0-106
  public Indexer() {
   // this.shooter = shooter;
    front = new TalonFX(39);
    pid = new Slot0Configs();
      pid.kP = 0.1; // change this if needed
      pid.kI = 0.0;
      pid.kD = 0.0;
      pid.kV = 0.12; // do not change
    TalonFXConfiguration frontfig = new TalonFXConfiguration();
    frontfig.Slot0 = pid;
    frontfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    frontfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    frontfig.CurrentLimits.SupplyCurrentLimit =40;
    frontfig.CurrentLimits.SupplyCurrentLimitEnable = true;
   
    front.getConfigurator().apply(frontfig);
    index = new VelocityVoltage(0);
  }
  public void runIndexer(){
        
    front.setControl(index.withVelocity(-targetSpeed));
  }
    
  public void stop(){
       front.setControl(index.withVelocity(0));
  }
  
  @Override
  public void periodic() {

      SmartDashboard.putNumber("F Indexer Speed", front.getVelocity().getValueAsDouble());
     
      SmartDashboard.putNumber("Target Speed", targetSpeed);
    // This method will be called once per scheduler run
  }
}
