/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private TalonSRX motor;
  private double perc = 0.0;
  private double maxRPM = 4600;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    motor = new TalonSRX(1);

    motor.setNeutralMode(NeutralMode.Coast);

    motor.enableCurrentLimit(true);
    motor.configPeakCurrentLimit(40, 10);
    motor.configContinuousCurrentLimit(30, 10);
    motor.configPeakCurrentDuration(100, 10);

    motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    motor.setSelectedSensorPosition(0, 0, 10);

    motor.config_kP(0, 0.25/*0.05*/, 10);
    motor.config_kI(0, 0.001, 10);
    motor.config_kD(0, 20, 10);
    motor.config_kF(0, 1 / (7200 / 4), 10);

    SmartDashboard.putNumber("Shooter/speed", perc); SmartDashboard.setPersistent("Shooter/speed");

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    perc = SmartDashboard.getNumber("Shooter/speed", 0.1);

    SmartDashboard.putNumber("Shooter/readSpeed", getVel());
    SmartDashboard.putNumber("Shooter/percentOut", motor.getMotorOutputPercent());
  }

  @Override
  public void disabledInit() {
    setVel(0);
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    //double revsPerSecond = 5;
    //int ticksPerRev = 1024;
    //int deciToBaseFactor = 10;
    //setVel((ticksPerRev * revsPerSecond) / deciToBaseFactor);
    setVel(3000);
  }

  public void setVel(double rpm) {
    double rpmToInternal = 1 / 0.1465;
    motor.set(ControlMode.Velocity, rpm * rpmToInternal);
  }

  public double getVel() {
    double internalCountToRpm = 0.1465;
    double revsPerSecondMotorSide = internalCountToRpm * motor.getSelectedSensorVelocity();// / (1024 * 4); //(motor.getSelectedSensorVelocity(0) / (1024 * 4)) * 10;
    return revsPerSecondMotorSide;
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
