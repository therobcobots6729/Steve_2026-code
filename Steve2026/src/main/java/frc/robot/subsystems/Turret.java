// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.revrobotics.RelativeEncoder;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turret extends SubsystemBase {
  private  SparkMax turnMotor;
 

  private static RelativeEncoder encoder;
  
  private Angle angle;
  public double arouund = 2;
  //private Limelight limelight;
  private Encoder absAngle;
  
  private  PIDController controller = new PIDController(0.02, 0, 0);//tune this a little more to stop the shakes
  
  /** Creates a new Turret. */
  public Turret( Angle angle) {
    //this.limelight = limelight;
    this.angle = angle;
    turnMotor  = new SparkMax(17, MotorType.kBrushless);
    controller.enableContinuousInput(-180, 180);
    controller.setTolerance(1.0);
    encoder = turnMotor.getEncoder();
    absAngle = new Encoder(1,0,false,Encoder.EncodingType.k2X);
    absAngle.setDistancePerPulse(360/2048/10);
      
  }
   public double getAngle(){
     double targetAngle = 360*encoder.getPosition()/90;
     return targetAngle;
  }
 
  
  public void stop(){
    turnMotor.set(0);
  }
  private boolean isConnected(){
    return !absAngle.getStopped();
  }
  private double TrueAngle(){
    return (absAngle.get())-0;//0 is an offset
  }

 public void runTurrent(){
     
    double error = angle.turret_Target();  // tx + lead

   // double turretAngle = getAngle(); // [-180, 180]

    // normal control
    double commandedError = error;

    // limit logic with forced wrap
    /*if (turretAngle >= 90 && commandedError > 0) {
        // blocked going positive → go full turn negative
        commandedError = error - 340;
    }
    else if (turretAngle <= -90 && commandedError < 0) {
        // blocked going negative → go full turn positive
        commandedError = error + 340;
    }*/

    double output = controller.calculate(0-commandedError);
    output = MathUtil.clamp(output, -1.0, 1.0);

    turnMotor.set(output);
   
}
public void looking(){
 
    double output = controller.calculate(0-arouund);
    output = MathUtil.clamp(output, -1.0, 1.0);

    turnMotor.set(output);
}
private void updatelook(){
  double turretAngle = getAngle();
  
  
  if (turretAngle >= 90) {
        // blocked going positive → go full turn negative
        arouund = 2;
    }
    else if (turretAngle <= -90) {
        // blocked going negative → go full turn positive
        arouund = - 2;
        
    }
    }
  


  @Override
  public void periodic() {
    if (getAngle() >= 90 || getAngle() <= -90){
    updatelook();}
    if (isConnected()){
      encoder.setPosition(TrueAngle()*9/360);
    }
    SmartDashboard.putNumber("Turret Angle", getAngle());
    SmartDashboard.putNumber("Tmotor", encoder.getPosition());
    SmartDashboard.putNumber("Encoder Offset", TrueAngle());
    // This method will be called once per scheduler run
  }
}
