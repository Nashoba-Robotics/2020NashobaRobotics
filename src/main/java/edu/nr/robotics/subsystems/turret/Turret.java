package edu.nr.robotics.subsystems.turret;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.motorcontrollers.CTRECreator;
import edu.nr.lib.motorcontrollers.SparkMax;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Acceleration;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularAcceleration;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Angle.Unit;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.DigitalSensor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Turret extends NRSubsystem
{
    private static Turret singleton;
    //private TalonSRX turretTalon;
    private CANSparkMax turretSpark;

    //TODO: Change below, speed and velocity to angular speed and velocity

    public static final double ENCODER_TICKS_PER_DEGREE_TALON = 0.0;
    public static final double ENCODER_TICKS_PER_DEGREE_SPARK = 0.0;
	public static final AngularAcceleration MAX_ACCEL = new AngularAcceleration(580, Angle.Unit.DEGREE, Time.Unit.SECOND, Time.Unit.SECOND);
	public static final AngularSpeed MAX_SPEED = new AngularSpeed(80, Angle.Unit.DEGREE, Time.Unit.SECOND);
    public static final double VOLTAGE_VELOCITY_SLOPE = 0.0;
    public static Time VOLTAGE_RAMP_RATE_TURRET = new Time(0.05, Time.Unit.SECOND);
    public static final int VOLTAGE_COMPENSATION_LEVEL = 12;
    public static final double MIN_MOVE_VOLTAGE = 0.0;
    public static final int DEFAULT_TIMEOUT = 0;

    public static final double PROFILE_VEL_PERCENT_TURRET = 0.0; //change
    public static final double PROFILE_ACCEL_PERCENT_TURRET = 0.0;

    public DigitalSensor limSwitchLeft = new DigitalSensor(RobotMap.LIM_TURRET_LEFT); // change IDs
    public DigitalSensor limSwitchRight = new DigitalSensor(RobotMap.LIM_TURRET_RIGHT);

    public final Angle leftmost = new Angle(-118, Angle.Unit.DEGREE);
    public final Angle rightmost = new Angle(118, Angle.Unit.DEGREE);

    //Changing these

    public static double F_POS_TURRET = 0;
    public static double P_POS_TURRET = 0;
    public static double I_POS_TURRET = 0;
    public static double D_POS_TURRET = 0;

    public static double F_VEL_TURRET = 0;
    public static double P_VEL_TURRET = 0;
    public static double I_VEL_TURRET = 0;
    public static double D_VEL_TURRET = 0;

    public static final int PEAK_CURRENT_TURRET = 60;
    public static final int CONTINUOUS_CURRENT_LIMIT_TURRET = 40;

    public static final NeutralMode NEUTRAL_MODE_TURRET = NeutralMode.Brake;

    //Type of PID. 0 = primary. 1 = cascade
    public static final int PID_TYPE = 0;

    public static final int VEL_SLOT = 0;
    public static final int POS_SLOT = 1;

    private AngularSpeed speedSetPoint = AngularSpeed.ZERO;

    public static Angle setAngle = Angle.ZERO;
    public static Angle deltaAngle = Angle.ZERO;
    public static Angle goalAngle = Angle.ZERO;

    public static final int MOTION_MAGIC_SLOT = 2;

    private Turret()
    {
        if(EnabledSubsystems.TURRET_ENABLED) {
            //turretTalon = CTRECreator.createMasterTalon(RobotMap.TURRET_TALON);
            turretSpark = SparkMax.createSpark(0, true);

            //turretTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PID_TYPE, DEFAULT_TIMEOUT);
            
            //turretTalon.config_kF(VEL_SLOT, 0, DEFAULT_TIMEOUT);
            //turretTalon.config_kP(VEL_SLOT, P_VEL_TURRET, DEFAULT_TIMEOUT);
            //turretTalon.config_kI(VEL_SLOT, I_VEL_TURRET, DEFAULT_TIMEOUT);
            //turretTalon.config_kD(VEL_SLOT, D_VEL_TURRET, DEFAULT_TIMEOUT);
            //turretTalon.config_kF(MOTION_MAGIC_SLOT, F_POS_TURRET, DEFAULT_TIMEOUT);
            //turretTalon.config_kP(MOTION_MAGIC_SLOT, P_POS_TURRET, DEFAULT_TIMEOUT);
            //turretTalon.config_kI(MOTION_MAGIC_SLOT, I_POS_TURRET, DEFAULT_TIMEOUT);
            //turretTalon.config_kD(MOTION_MAGIC_SLOT, D_POS_TURRET, DEFAULT_TIMEOUT);

            //turretTalon.config_kF(POS_SLOT, F_POS_TURRET, DEFAULT_TIMEOUT);
            //turretTalon.config_kP(POS_SLOT, P_POS_TURRET, DEFAULT_TIMEOUT);
            //turretTalon.config_kI(POS_SLOT, I_POS_TURRET, DEFAULT_TIMEOUT);
            //turretTalon.config_kD(POS_SLOT, D_POS_TURRET, DEFAULT_TIMEOUT);

            //turretTalon.setNeutralMode(NEUTRAL_MODE_TURRET);

            //turretTalon.setInverted(false);
            //Change to Talon Version
            //turretTalon.setSensorPhase(false);

            //turretTalon.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_TURRET);

            //turretTalon.enableVoltageCompensation(true);
            //turretTalon.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);
            
            //turretTalon.enableCurrentLimit(true);
            //turretTalon.configPeakCurrentLimit(PEAK_CURRENT_TURRET, DEFAULT_TIMEOUT);
            //turretTalon.configPeakCurrentDuration(CONTINUOUS_CURRENT_LIMIT_TURRET, DEFAULT_TIMEOUT);

            //turretTalon.configClosedloopRamp(VOLTAGE_RAMP_RATE_TURRET.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
            //turretTalon.configOpenloopRamp(VOLTAGE_RAMP_RATE_TURRET.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);

            //Change Magnetic_encoder_tick_aux_drive to angle stuff, angular acc and velocity
            //turretTalon.configMotionCruiseVelocity((int) MAX_SPEED.mul(PROFILE_VEL_PERCENT_TURRET).get(Angle.Unit.TURRET_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND), DEFAULT_TIMEOUT);
            //turretTalon.configMotionAcceleration((int) MAX_ACCEL.mul(PROFILE_ACCEL_PERCENT_TURRET).get(Angle.Unit.TURRET_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND, Time.Unit.HUNDRED_MILLISECOND), DEFAULT_TIMEOUT);

            //turretTalon.getSensorCollection().setQuadraturePosition(0, DEFAULT_TIMEOUT);

            if(EnabledSubsystems.TURRET_DUMB_ENABLED) {
                //turretTalon.set(ControlMode.PercentOutput, 0);
                turretSpark.set(0);
            }
            else{
                //turretTalon.set(ControlMode.Velocity, 0);
                turretSpark.set(0);
            }
        }
        smartDashboardInit();
    }

    public synchronized static void init()
    {
        if(singleton == null)
            singleton = new Turret();
    }

    public static Turret getInstance()
    {
        if(singleton == null)
            init();
        return singleton;
    }

    public void disable()
    {
        /*
        if(turretTalon != null){
        turretTalon.set(ControlMode.PercentOutput, 0);
        setAngle(getAngle());
        */
        if(turretSpark != null)
        {
            turretSpark.stopMotor();
            setAngle(getAngle());
        }
    }

    public void smartDashboardInit()
    {
        if(EnabledSubsystems.TURRET_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putNumber("Turret Profile Delta Inches: ", 0);
            SmartDashboard.putNumber("Voltage Ramp Rate Turret Seconds", VOLTAGE_RAMP_RATE_TURRET.get(Time.Unit.SECOND));

            SmartDashboard.putNumber("F_POS_TURRET: ", F_POS_TURRET);
            SmartDashboard.putNumber("P_POS_TURRET: ", P_POS_TURRET);
            SmartDashboard.putNumber("I_POS_TURRET: ", I_POS_TURRET);
            SmartDashboard.putNumber("D_POS_TURRET: ", D_POS_TURRET);

            SmartDashboard.putNumber("F_VEL_TURRET: ", F_VEL_TURRET);
            SmartDashboard.putNumber("P_VEL_TURRET: ", P_VEL_TURRET);
            SmartDashboard.putNumber("I_VEL_TURRET: ", I_VEL_TURRET);
            SmartDashboard.putNumber("D_VEL_TURRET: ", D_VEL_TURRET);

            SmartDashboard.putNumber("Profile Vel Percent Turret: ", PROFILE_VEL_PERCENT_TURRET);
            SmartDashboard.putNumber("Profile Accel Percent Turret: ", PROFILE_ACCEL_PERCENT_TURRET);

            SmartDashboard.putNumber("Target Angle: ", goalAngle.get(Angle.Unit.DEGREE));
            SmartDashboard.putNumber("Delta Angle: ", deltaAngle.get(Angle.Unit.DEGREE));

        }
    }

    public void smartDashboardInfo()
    {
        if(turretSpark != null)
        {
            if(EnabledSubsystems.TURRET_SMARTDASHBOARD_BASIC_ENABLED)
            {
                SmartDashboard.putNumber("Turret Position: ", getAngle().get(Angle.Unit.DEGREE));
                //SmartDashboard.putNumber("Turret Current", turretTalon.getStatorCurrent());
                SmartDashboard.putNumber("Turret Current: ", turretSpark.getOutputCurrent());
				SmartDashboard.putString("Turret Speed", getSpeed().get(Angle.Unit.DEGREE, Time.Unit.SECOND) + " : " + speedSetPoint.get(Angle.Unit.DEGREE, Time.Unit.SECOND));
				SmartDashboard.putString("Turret Angle", getAngle().get(Angle.Unit.DEGREE) + " : " + setAngle.get(Angle.Unit.DEGREE));	
			}
			if(EnabledSubsystems.TURRET_SMARTDASHBOARD_DEBUG_ENABLED){
				//SmartDashboard.putString("Turret Control Mode", turretTalon.getControlMode().toString());
				//SmartDashboard.putNumber("Turret Voltage", turretTalon.getMotorOutputVoltage());
                //SmartDashboard.putNumber("Turret Raw Position Ticks", turretTalon.getSelectedSensorPosition());

                SmartDashboard.putNumber("Turret Output Current: ", turretSpark.getOutputCurrent());
                SmartDashboard.putNumber("Turret Raw Position Ticks: ", turretSpark.getEncoder().getPosition());
                
                goalAngle = new Angle(SmartDashboard.getNumber("Target Angle: ", goalAngle.get(Angle.Unit.DEGREE)), Angle.Unit.DEGREE);
                deltaAngle = new Angle(SmartDashboard.getNumber("Delta Angle: ", deltaAngle.get(Angle.Unit.DEGREE)), Angle.Unit.DEGREE);
			}
        }
       // System.out.println(goalAngle.get(Angle.Unit.DEGREE));


    }

    public Angle getAngle()
    {
        /*
        if(turretTalon != null)
        {
            return new Angle(turretTalon.getSelectedSensorPosition() / ENCODER_TICKS_PER_DEGREE_TALON, Angle.Unit.DEGREE);
        }
        */
        if(turretSpark != null)
        {
            return new Angle(turretSpark.getEncoder().getPosition() / ENCODER_TICKS_PER_DEGREE_SPARK, Angle.Unit.DEGREE);
        }
        return Angle.ZERO;
    }

    public void setMotorSpeedInPercent(double percent){
        //turretTalon.set(ControlMode.PercentOutput, percent);

        if(turretSpark != null)
        turretSpark.set(percent);
    }

    public void setMotorSpeedInDegreesPerSecond(AngularSpeed speed){
        speedSetPoint = speed;
        //double ratio = speed.get(Unit.DEGREE, Time.Unit.SECOND) / MAX_SPEED.get(Unit.DEGREE, Time.Unit.SECOND);
        double ratio = speed.get(Unit.DEGREE, Time.Unit.SECOND) / MAX_SPEED.get(Unit.DEGREE, Time.Unit.SECOND);
        /*
        if(turretTalon != null){
            turretTalon.set(ControlMode.PercentOutput, ratio);
        }
        */
        if(turretSpark != null)
        {
            turretSpark.set(ratio);
        }

    }

    public AngularSpeed getSpeed(){
        /*
        if(turretTalon != null){
            return speedSetPoint;
        }
        */
        if(turretSpark != null)
        {
            return speedSetPoint;
        }

        return AngularSpeed.ZERO;
    }

    public void setAngle(Angle targetAngle){
        goalAngle = targetAngle;
        //turretTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);

        //turretTalon.set(ControlMode.Position, goalAngle.get(Unit.TURRET_ENCODER_TICK));

        turretSpark.getEncoder().setPosition(targetAngle.get(Unit.TURRET_ENCODER_TICK));

    }

    public void periodic()
    {
        /*
        if(turretTalon != null)
        {
            if(limSwitchLeft.get()){
                setAngle(leftmost);
            }
            if(limSwitchRight.get()){
                setAngle(rightmost);
            }
        }
        */

        if(turretSpark != null)
        {
            if(limSwitchLeft.get())
            {
                setAngle(leftmost);
            }
            else if(limSwitchRight.get())
            {
                setAngle(rightmost);
            }
        }
    //    System.out.println(goalAngle.get(Angle.Unit.DEGREE));
    }

    public double getCurrent(){
        /*
        if(turretTalon != null){
            return turretTalon.getStatorCurret();
        }
        */
        if(turretSpark != null)
        {
            return turretSpark.getOutputCurrent();
        }
        return 0;
    }



    
}