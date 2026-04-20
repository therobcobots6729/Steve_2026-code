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
  public double arouund = 10;
  //private Limelight limelight;
  private Swerve swerve;
  private Encoder absAngle;
  
  private  PIDController controller = new PIDController(0.01125, 0, 0.0);//.01125//tune this a little more to stop the shakes
  private  PIDController position = new PIDController(0.015  , 0, 0);// tune this for feeding turret
  /** Creates a new Turret. */
  public Turret( Angle angle, Swerve swerve) {
    //this.limelight = limelight;
    this.angle = angle;
    this.swerve =swerve;
    turnMotor  = new SparkMax(17, MotorType.kBrushless);
    controller.enableContinuousInput(-180, 180);
    controller.setTolerance(1.0);
    encoder = turnMotor.getEncoder();
    absAngle = new Encoder(0,1,false,Encoder.EncodingType.k2X);
    absAngle.setDistancePerPulse(.017578125);
      
  }
   public static double getAngle(){
     double targetAngle = 360*encoder.getPosition()/90; // turret clockwise is positive         motor is moving negative
     return targetAngle;
  }
 
  
  public void stop(){
    turnMotor.set(0);
  }
  private boolean isConnected(){
    return !absAngle.getStopped();
  }
  private double TrueAngle(){
    return absAngle.getDistance(); // turret clockwise is positive         motor negative
  }
  public void Zero(){
    double output = controller.calculate(0-getAngle());
    output = MathUtil.clamp(output, -1.0, 1.0);
    turnMotor.set(output);
  }
   public void feeder(){
   double heading =swerve.Rotation()+180;
   double wrap = MathUtil.inputModulus(heading, -180, 180);
      /*if(wrap<90 && wrap>-90){
        double output = position.calculate(wrap-TrueAngle());
        turnMotor.set(-output);//flip if backwards
      }*/
     // else{
        double output = position.calculate(0-TrueAngle());
        turnMotor.set(-output);
      
  }
 public void runTurrent(){
     
    double error = angle.turret_Target();  // tx + lead
    double output;

    // normal control
    double commandedError = error;

    if(Limelight.validTarget()){
     output = controller.calculate(0-commandedError);}
    else{ output=0;}
    output = MathUtil.clamp(output, -1.0, 1.0);
    if (output + TrueAngle()<90|| output+ TrueAngle()>-90){
    turnMotor.set(output);}
    else{turnMotor.set(0);}
   
}
public void looking(){
 
    double output = controller.calculate(arouund);
    output = MathUtil.clamp(output, -1.0, 1.0);

    turnMotor.set(output);
}
public boolean Connected(){
  if (encoder.getVelocity() != 0 && absAngle.getStopped()){
    return false;
  }
  else {
    return true;
  }

}
private void updatelook(){
  double trueAngle = TrueAngle();
  double turretAngle = getAngle();
  double Angle;

  if (isConnected()){
    Angle = trueAngle;
  }
  else{
    Angle = turretAngle;
  }
  
  
  if (Angle >= 90) {
        // blocked going positive → go full turn negative
        arouund =10;
    }
    else if (Angle <= -90) {
        // blocked going negative → go full turn positive
        arouund =  -10;
        
    }
    }
  


  @Override
  public void periodic() {
    if (getAngle() >= 90 || getAngle() <= -90){
    updatelook();}
    
    SmartDashboard.putNumber("Turret Angle", getAngle());
    SmartDashboard.putNumber("Tmotor", encoder.getPosition());
    SmartDashboard.putNumber("Encoder Offset", TrueAngle());
    SmartDashboard.putBoolean("connected", isConnected());
    SmartDashboard.putBoolean("test", Connected());
    // This method will be called once per scheduler run
  }
}
