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

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */
  private  TalonFX shooty;
  private  VelocityVoltage shoot;
  private  Slot0Configs pid;
  private Velocity velocity;
  private boolean hadTargetLastLoop = false;

  
  private LinearFilter distanceFilter = LinearFilter.movingAverage(5);



  public Shooter(Velocity velocity) {
    this.velocity = velocity;
    
    shooty = new TalonFX(32);
    
     pid = new Slot0Configs();
      pid.kP = 0.1; // change this if needed
      pid.kI = 0.0;
      pid.kD = 0.0;
      pid.kV = 0.12; //  (volts per rps) do not change
      pid.kA = 0.01;

    TalonFXConfiguration config = new TalonFXConfiguration();
    config.Slot0 = pid;
    config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    config.CurrentLimits.SupplyCurrentLimit = 40;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;

    shooty.getConfigurator().apply(config);

   shoot = new VelocityVoltage(0);
    
  }
 

  public boolean atSpeed(){
    return Math.abs(shooty.getVelocity().getValueAsDouble() - distanceFilter.calculate(velocity.outputSpeed())) < 2.0;
}

  public void runShooter() {
    boolean hasTarget = velocity.outputSpeed()> 0;

    if(hasTarget && !hadTargetLastLoop){
       distanceFilter.reset();
}
  


    hadTargetLastLoop = hasTarget; 

     if (velocity.outputSpeed()>0 && velocity.outputSpeed() <=105){
     double filteredDistance = distanceFilter.calculate(velocity.outputSpeed());
     shooty.setControl(shoot.withVelocity(filteredDistance));}

     else{
      stop();
     }

}
public void runShooty(){
    shooty.setControl(shoot.withVelocity(velocity.inputSpeed())); // 45.5-47at 23.0   52went in w/o touching sides-55 at 34  46 at 29.4 went in w/o touching sides
  }                                                         //  46-48 at 26.3   49-52at 31.9


  public void stop() {
  shooty.stopMotor();
  }

  @Override
  public void periodic() {
   
    
    SmartDashboard.putBoolean("Fire?", atSpeed());
    SmartDashboard.putNumber("Actual Shooter velocity", shooty.getVelocity().getValueAsDouble());
    SmartDashboard.putNumber("Filtered Distance", distanceFilter.calculate(velocity.outputSpeed()));
    
    // This method will be called once per scheduler run
  }
}
