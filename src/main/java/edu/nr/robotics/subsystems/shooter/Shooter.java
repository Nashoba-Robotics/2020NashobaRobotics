package edu.nr.robotics.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.motorcontrollers.CTRECreator;
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
    private static Shooter singleton;

    private TalonSRX shooterTalon;

    public static final double ENCODER_TICKS_PER_DEGREE_SHOOTER = 2048 / 360;


    public static Time VOLTAGE_RAMP_RATE_SHOOTER = new Time(0.05, Time.Unit.SECOND);
    public static final int VOLTAGE_COMPENSATION_LEVEL = 12;
    public static final double MIN_MOVE_VOLTAGE = 0.0;
    public static final int DEFAULT_TIMEOUT = 0;
    public static final double RUN_PERCENT = 0.6;

    public static double F_VEL_SHOOTER = 0;
    public static double P_VEL_SHOOTER = 0;
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

    public static final int PID_TYPE = 0;

    public static final int VEL_SLOT = 0;
    public static final int POS_SLOT = 1;

    private Shooter()
    {
        if(EnabledSubsystems.SHOOTER_ENABLED) {
            shooterTalon = CTRECreator.createMasterTalon(RobotMap.SHOOTER_TALON);

            shooterTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PID_TYPE, DEFAULT_TIMEOUT);
            
            shooterTalon.config_kF(VEL_SLOT, 0, DEFAULT_TIMEOUT);
            shooterTalon.config_kP(VEL_SLOT, P_VEL_SHOOTER, DEFAULT_TIMEOUT);
            shooterTalon.config_kI(VEL_SLOT, I_VEL_SHOOTER, DEFAULT_TIMEOUT);
            shooterTalon.config_kD(VEL_SLOT, D_VEL_SHOOTER, DEFAULT_TIMEOUT);

            shooterTalon.setNeutralMode(NEUTRAL_MODE_SHOOTER);

            shooterTalon.setInverted(false);
            //Change to Talon Version
            shooterTalon.setSensorPhase(false);

            shooterTalon.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_SHOOTER);

            shooterTalon.enableVoltageCompensation(true);
            shooterTalon.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);
            
            shooterTalon.enableCurrentLimit(true);
            shooterTalon.configPeakCurrentLimit(PEAK_CURRENT_SHOOTER, DEFAULT_TIMEOUT);
            shooterTalon.configPeakCurrentDuration(CONTINUOUS_CURRENT_LIMIT_SHOOTER, DEFAULT_TIMEOUT);

            shooterTalon.configClosedloopRamp(VOLTAGE_RAMP_RATE_SHOOTER.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
            shooterTalon.configOpenloopRamp(VOLTAGE_RAMP_RATE_SHOOTER.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);

            shooterTalon.getSensorCollection().setQuadraturePosition(0, DEFAULT_TIMEOUT);

            if(EnabledSubsystems.SHOOTER_DUMB_ENABLED) {
                shooterTalon.set(ControlMode.PercentOutput, 0);
            }
            else{
                shooterTalon.set(ControlMode.Velocity, 0);
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
        if(shooterTalon != null)
            shooterTalon.set(ControlMode.PercentOutput, 0.0);

    }

    public void smartDashboardInit(){

        if(EnabledSubsystems.SHOOTER_SMARTDASHBOARD_DEBUG_ENABLED){
        SmartDashboard.putNumber("F_VEL_SHOOTER", F_VEL_SHOOTER);
        SmartDashboard.putNumber("P_VEL_SHOOTER", P_VEL_SHOOTER);
        SmartDashboard.putNumber("I_VEL_SHOOTER", I_VEL_SHOOTER);
        SmartDashboard.putNumber("D_VEL_SHOOTER", D_VEL_SHOOTER);

        SmartDashboard.putNumber("Shooter Goal Speed", goalSpeed.get(Angle.Unit.DEGREE, Time.Unit.SECOND));
        
        }
    }

    public void smartDashboardInfo()
    {
        if(shooterTalon != null){

            if(EnabledSubsystems.SHOOTER_SMARTDASHBOARD_BASIC_ENABLED){
                SmartDashboard.putNumber("Shooter Speed", getSpeed().get(Angle.Unit.DEGREE, Time.Unit.SECOND));
                SmartDashboard.putNumber("Shooter Current", shooterTalon.getStatorCurrent());
                
                F_VEL_SHOOTER = SmartDashboard.getNumber("F_VEL_SHOOTER", 0);
                P_VEL_SHOOTER = SmartDashboard.getNumber("P_VEL_SHOOTER", 0);
                I_VEL_SHOOTER = SmartDashboard.getNumber("I_VEL_SHOOTER", 0);
                D_VEL_SHOOTER = SmartDashboard.getNumber("D_VEL_SHOOTER", 0);

                shooterTalon.config_kF(VEL_SLOT, 0, DEFAULT_TIMEOUT);
                shooterTalon.config_kP(VEL_SLOT, P_VEL_SHOOTER, DEFAULT_TIMEOUT);
                shooterTalon.config_kI(VEL_SLOT, I_VEL_SHOOTER, DEFAULT_TIMEOUT);
                shooterTalon.config_kD(VEL_SLOT, D_VEL_SHOOTER, DEFAULT_TIMEOUT);

            }
            if(EnabledSubsystems.SHOOTER_SMARTDASHBOARD_DEBUG_ENABLED){
                SmartDashboard.putString("Shooter Control Mode", shooterTalon.getControlMode().toString());
				SmartDashboard.putNumber("Shooter Voltage", shooterTalon.getMotorOutputVoltage());
                SmartDashboard.putNumber("Shooter Raw Position Ticks", shooterTalon.getSelectedSensorPosition());
            }
            goalSpeed = new AngularSpeed( SmartDashboard.getNumber("Shooter Goal Speed", goalSpeed.get(Angle.Unit.DEGREE, Time.Unit.SECOND)), Angle.Unit.DEGREE, Time.Unit.SECOND);
        }
    }

    public AngularSpeed getSpeed(){
        if(shooterTalon != null){
            return new AngularSpeed(shooterTalon.getSelectedSensorVelocity(), Angle.Unit.MAGNETIC_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND);
        }
        return AngularSpeed.ZERO;
    }

    public void setMotorSpeed(AngularSpeed speed)
    {
        if(shooterTalon != null){
            shooterTalon.selectProfileSlot(VEL_SLOT, DEFAULT_TIMEOUT);
            speedSetPointShooter = speed;
            shooterTalon.set(ControlMode.Velocity, speedSetPointShooter.get(Angle.Unit.MAGNETIC_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND));
        }
    }

    public void setMotorSpeedRaw(double percent) {
        if(shooterTalon != null)
        shooterTalon.set(ControlMode.PercentOutput, percent);
    }

}