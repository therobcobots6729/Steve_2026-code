// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Flipper extends SubsystemBase {
  /** Creates a new Flipper. */
  private TalonFX flippy;
  private DutyCycleEncoder intakeAngle;
  private double offset = 180; // in degrees with 0 being level to the ground, make moviing up positive
  private double angle = 0; //dont touch for testing purposes only
  private double up,raised,down,half;
  public Flipper() {
    flippy = new TalonFX(31);
    TalonFXConfiguration config = new TalonFXConfiguration();
    
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;//change if motor backwards
    config.CurrentLimits.SupplyCurrentLimit = 40;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    flippy.getConfigurator().apply(config);

    intakeAngle  = new DutyCycleEncoder(2);
    intakeAngle.setInverted(true);
    up = 85;
    down  = 0;
    raised = 70;
    half = 45;
    
  }

  private double actualAngle(){
      double Angle = intakeAngle.get()*360  - offset;
    return Angle;
  }
  private double output(double target){

    double base = 0.025*Math.cos(Math.toRadians(actualAngle()));////scalar for static hold
    double push = .0005*(actualAngle()-target); // scalar for moving tune after static hold
    return -push + base;
  }
  public void halt(){
      flippy.set(output(actualAngle()));
      //flippy.set(.05);
  }
  public void agitate(){
    if (actualAngle()<=half){
        flippy.set(output(raised));
    }
    else if (actualAngle()>=raised){
      flippy.set(output(half));
    }
  }
  public void raise(){
    flippy.set(output(up));
  }
  public void lower(){
    if (actualAngle()>=down+10){
    flippy.set(output(down));}
    else {
      stop();
    }
  }
  public void stop(){
    flippy.set(0);
  }
  @Override
  public void periodic() {
    
    SmartDashboard.putNumber("Intake Angle", actualAngle());
    // This method will be called once per scheduler run
  }
}
