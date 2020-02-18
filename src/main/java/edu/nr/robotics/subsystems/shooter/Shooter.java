package edu.nr.robotics.subsystems.shooter;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.motorcontrollers.CTRECreator;
import edu.nr.lib.motorcontrollers.SparkMax;
import edu.nr.lib.units.AngularAcceleration;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Angle.Unit;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.DigitalSensor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
 
public class Shooter extends NRSubsystem
{
  //Needs to be done correctly
  private static Shooter singleton;
  private TalonFX shooterTalon1;
  private TalonFX shooterTalon2;
  public static final double ENCODER_TICKS_PER_DEGREE_SHOOTER = 2048 / 360;
  public static Time VOLTAGE_RAMP_RATE_SHOOTER = new Time(0.05, Time.Unit.SECOND);
  public static final int VOLTAGE_COMPENSATION_LEVEL = 12;
  public static final double MIN_MOVE_VOLTAGE = 0.0;
  public static final int DEFAULT_TIMEOUT = 0;
  public static final double RUN_PERCENT = 0.6;
  public static final Time PUKE_TIME = new Time(0.8, Time.Unit.SECOND );
  public static double F_VEL_SHOOTER = 0.05;
  public static double P_VEL_SHOOTER = 0.00;
  public static double I_VEL_SHOOTER = 0;
  public static double D_VEL_SHOOTER = 0;
  public static final int PEAK_CURRENT_SHOOTER = 60;
  public static final int CONTINUOUS_CURRENT_LIMIT_SHOOTER = 40;
  public static final NeutralMode NEUTRAL_MODE_SHOOTER = NeutralMode.Coast;
  public static AngularSpeed speedSetPointShooter = AngularSpeed.ZERO;
  public static AngularSpeed goalSpeed = AngularSpeed.ZERO;
  public static final int MOTION_MAGIC_SLOT = 2;
  public static final AngularAcceleration MAX_ACCEL = new AngularAcceleration(580, Angle.Unit.DEGREE, Time.Unit.SECOND, Time.Unit.SECOND);
  public static final AngularSpeed MAX_SPEED = new AngularSpeed(800, Angle.Unit.DEGREE, Time.Unit.SECOND);
  public static final AngularSpeed CRUISE_SPEED = AngularSpeed.ZERO;
  public static final AngularSpeed SHOOT_SPEED = new AngularSpeed(5000, Angle.Unit.ROTATION, Time.Unit.MINUTE);
  public static final int PID_TYPE = 0;
  public static final int VEL_SLOT = 0;
  public static final int POS_SLOT = 1;

  public static final double PUKE_PERCENT = -0.6;

  private Shooter()
  {
      if(EnabledSubsystems.SHOOTER_ENABLED) {
          shooterTalon1 = new TalonFX(RobotMap.SHOOTER_TALON1);
          shooterTalon2 = new TalonFX(RobotMap.SHOOTER_TALON2);
          shooterTalon2.set(ControlMode.Follower, RobotMap.SHOOTER_TALON1);
          shooterTalon1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, PID_TYPE, DEFAULT_TIMEOUT);
          shooterTalon2.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, PID_TYPE, DEFAULT_TIMEOUT);
        
          shooterTalon1.config_kF(VEL_SLOT, F_VEL_SHOOTER, DEFAULT_TIMEOUT);
          shooterTalon1.config_kP(VEL_SLOT, P_VEL_SHOOTER, DEFAULT_TIMEOUT);
          shooterTalon1.config_kI(VEL_SLOT, I_VEL_SHOOTER, DEFAULT_TIMEOUT);
          shooterTalon1.config_kD(VEL_SLOT, D_VEL_SHOOTER, DEFAULT_TIMEOUT);
          shooterTalon2.config_kF(VEL_SLOT, F_VEL_SHOOTER, DEFAULT_TIMEOUT);
          shooterTalon2.config_kP(VEL_SLOT, P_VEL_SHOOTER, DEFAULT_TIMEOUT);
          shooterTalon2.config_kI(VEL_SLOT, I_VEL_SHOOTER, DEFAULT_TIMEOUT);
          shooterTalon2.config_kD(VEL_SLOT, D_VEL_SHOOTER, DEFAULT_TIMEOUT);
          shooterTalon1.setNeutralMode(NEUTRAL_MODE_SHOOTER);
          shooterTalon2.setNeutralMode(NEUTRAL_MODE_SHOOTER);
          shooterTalon1.setSensorPhase(false);
          shooterTalon2.setSensorPhase(false);
          shooterTalon1.setInverted(true);
          shooterTalon2.setInverted(false);
        
          //shooterTalon1.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_SHOOTER);
          //shooterTalon2.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_SHOOTER);
          shooterTalon1.enableVoltageCompensation(true);
          shooterTalon1.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);
          shooterTalon2.enableVoltageCompensation(true);
          shooterTalon2.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);
        
          //shooterTalon1.enableCurrentLimit(true);
          //shooterTalon1.configPeakCurrentLimit(PEAK_CURRENT_SHOOTER, DEFAULT_TIMEOUT);
          //shooterTalon1.configPeakCurrentDuration(CONTINUOUS_CURRENT_LIMIT_SHOOTER, DEFAULT_TIMEOUT);
          //shooterTalon2.enableCurrentLimit(true);
          //shooterTalon2.configPeakCurrentLimit(PEAK_CURRENT_SHOOTER, DEFAULT_TIMEOUT);
          //shooterTalon2.configPeakCurrentDuration(CONTINUOUS_CURRENT_LIMIT_SHOOTER, DEFAULT_TIMEOUT);
          shooterTalon1.configClosedloopRamp(VOLTAGE_RAMP_RATE_SHOOTER.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
          shooterTalon1.configOpenloopRamp(VOLTAGE_RAMP_RATE_SHOOTER.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
          shooterTalon2.configClosedloopRamp(VOLTAGE_RAMP_RATE_SHOOTER.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
          shooterTalon2.configOpenloopRamp(VOLTAGE_RAMP_RATE_SHOOTER.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
          shooterTalon1.getSensorCollection().setIntegratedSensorPosition(0, DEFAULT_TIMEOUT);
          shooterTalon2.getSensorCollection().setIntegratedSensorPosition(0, DEFAULT_TIMEOUT);
          if(EnabledSubsystems.SHOOTER_DUMB_ENABLED) {
              shooterTalon1.set(ControlMode.PercentOutput, 0);
              shooterTalon2.set(ControlMode.PercentOutput, 0);
          }
          else{
              shooterTalon1.set(ControlMode.Velocity, 0);
              shooterTalon2.set(ControlMode.Velocity, 0);
          }
      }
      smartDashboardInit();
  }
  public synchronized static void init()
  {
      if(singleton == null)
          singleton = new Shooter();
  }
  public static Shooter getInstance()
  {
      if(singleton == null)
          init();
      return singleton;
  }
  public void disable()
  {
      if(shooterTalon1 != null)
          shooterTalon1.set(ControlMode.PercentOutput, 0.0);
      if(shooterTalon2 != null)
          shooterTalon2.set(ControlMode.PercentOutput, 0.0);
  }
  public void smartDashboardInit(){
      //SmartDashboard.putNumber("TEST SHOOTER PERCENT", 0);
      if(EnabledSubsystems.SHOOTER_SMARTDASHBOARD_DEBUG_ENABLED){
      SmartDashboard.putNumber("Shooter Goal Speed", goalSpeed.get(Angle.Unit.ROTATION, Time.Unit.MINUTE));

      SmartDashboard.putNumber("Shooter Motor Percent", 0);
    
      SmartDashboard.putNumber("F_VEL_SHOOTER", F_VEL_SHOOTER);
      SmartDashboard.putNumber("P_VEL_SHOOTER", P_VEL_SHOOTER);
      SmartDashboard.putNumber("I_VEL_SHOOTER", I_VEL_SHOOTER);
      SmartDashboard.putNumber("D_VEL_SHOOTER", D_VEL_SHOOTER);
      }
  }
  public void smartDashboardInfo()
  {
      if(shooterTalon1 != null && shooterTalon2 != null){
          if(EnabledSubsystems.SHOOTER_SMARTDASHBOARD_BASIC_ENABLED){
              SmartDashboard.putNumber("Shooter1 Speed", getSpeedShooter1().get(Angle.Unit.ROTATION, Time.Unit.MINUTE));
              SmartDashboard.putNumber("Shooter2 Speed", getSpeedShooter2().get(Angle.Unit.ROTATION, Time.Unit.MINUTE));
            
              SmartDashboard.putNumber("Shooter 1 Position", shooterTalon1.getSelectedSensorPosition());
              SmartDashboard.putNumber("Shooter1 Current", shooterTalon1.getStatorCurrent());
              //SmartDashboard.putNumber("Shooter2 Speed", getSpeedShooter2().get(Angle.Unit.DEGREE, Time.Unit.SECOND));
              SmartDashboard.putNumber("Shooter2 Current", shooterTalon2.getStatorCurrent());
              setMotorSpeedInPercent(SmartDashboard.getNumber("Shooter Motor Percent", 0));
              F_VEL_SHOOTER = SmartDashboard.getNumber("F_VEL_SHOOTER", 0);
              P_VEL_SHOOTER = SmartDashboard.getNumber("P_VEL_SHOOTER", 0);
              I_VEL_SHOOTER = SmartDashboard.getNumber("I_VEL_SHOOTER", 0);
              D_VEL_SHOOTER = SmartDashboard.getNumber("D_VEL_SHOOTER", 0);
              shooterTalon1.config_kF(VEL_SLOT, F_VEL_SHOOTER, DEFAULT_TIMEOUT);
              shooterTalon1.config_kP(VEL_SLOT, P_VEL_SHOOTER, DEFAULT_TIMEOUT);
              shooterTalon1.config_kI(VEL_SLOT, I_VEL_SHOOTER, DEFAULT_TIMEOUT);
              shooterTalon1.config_kD(VEL_SLOT, D_VEL_SHOOTER, DEFAULT_TIMEOUT);
              shooterTalon2.config_kF(VEL_SLOT, F_VEL_SHOOTER, DEFAULT_TIMEOUT);
              shooterTalon2.config_kP(VEL_SLOT, P_VEL_SHOOTER, DEFAULT_TIMEOUT);
              shooterTalon2.config_kI(VEL_SLOT, I_VEL_SHOOTER, DEFAULT_TIMEOUT);
              shooterTalon2.config_kD(VEL_SLOT, D_VEL_SHOOTER, DEFAULT_TIMEOUT);
          }
          if(EnabledSubsystems.SHOOTER_SMARTDASHBOARD_DEBUG_ENABLED){
              SmartDashboard.putString("Shooter1 Control Mode", shooterTalon1.getControlMode().toString());
               SmartDashboard.putNumber("Shooter1 Voltage", shooterTalon1.getMotorOutputVoltage());
              SmartDashboard.putNumber("Shooter1 Raw Position Ticks", shooterTalon1.getSelectedSensorPosition());
              SmartDashboard.putString("Shooter2 Control Mode", shooterTalon2.getControlMode().toString());
               SmartDashboard.putNumber("Shooter2 Voltage", shooterTalon2.getMotorOutputVoltage());
              SmartDashboard.putNumber("Shooter2 Raw Position Ticks", shooterTalon2.getSelectedSensorPosition());
          }
          //shooterTalon1.set(ControlMode.PercentOutput, SmartDashboard.getNumber("Shooter Goal Speed", 0));
          goalSpeed = new AngularSpeed(SmartDashboard.getNumber("Shooter Goal Speed", goalSpeed.get(Angle.Unit.ROTATION, Time.Unit.MINUTE)), Angle.Unit.ROTATION, Time.Unit.MINUTE);
       //   setMotorSpeed(goalSpeed);
      }
  }
  public AngularSpeed getSpeedShooter1(){
      if(shooterTalon1 != null){
           return new AngularSpeed(shooterTalon1.getSelectedSensorVelocity(), Angle.Unit.MAGNETIC_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND);
      }
      return AngularSpeed.ZERO;
  }
   public AngularSpeed getSpeedShooter2()
  {
      if(shooterTalon2 != null)
      {
          return new AngularSpeed(shooterTalon2.getSelectedSensorVelocity(), Angle.Unit.MAGNETIC_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND);
      }
      return AngularSpeed.ZERO;
  }
 
  public void setMotorSpeed(AngularSpeed speed){
      if(shooterTalon1 != null){
          //SmartDashboard.putNumber("Commanding Shooter Speed", 1706);
          shooterTalon1.set(ControlMode.Velocity, speed.get(Angle.Unit.SHOOTER_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND));
          // * -1 ? for one of them?
          shooterTalon2.set(ControlMode.Velocity, speed.get(Angle.Unit.SHOOTER_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND));
      }
  }
  public void setMotorSpeedInPercent(double percent){
   shooterTalon1.set(ControlMode.PercentOutput, percent);
   shooterTalon2.set(ControlMode.PercentOutput, percent); // -1 * percent to invert?
  }
}
