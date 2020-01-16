package edu.nr.robotics.subsystems.hood;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.nr.lib.motorcontrollers.CTRECreator;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularAcceleration;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.sensors.DigitalSensor;

public class Hood {

    private TalonSRX hoodTalon;

    public static final double ENCODER_TICKS_PER_DEGREE_HOOD = 0.0;

    public static final int VOLTAGE_COMPENSATION_LEVEL = 12;
    public static final double MIN_MOVE_VOLTAGE = 0.0;
    public static final int DEFAULT_TIMEOUT = 0;
    public static Time VOLTAGE_RAMP_RATE_HOOD = new Time(0.05, Time.Unit.SECOND);
    public static final double PROFILE_VEL_PERCENT_HOOD = 0.0; //change
    public static final double PROFILE_ACCEL_PERCENT_HOOD = 0.0;

    public DigitalSensor limSwitchTop = new DigitalSensor(2); // change IDs
    public DigitalSensor limSwitchBottom = new DigitalSensor(3);

    public final Angle uppermost = new Angle(45, Angle.Unit.DEGREE);
    public final Angle lowermost = new Angle(0, Angle.Unit.DEGREE);

    public static double F_POS_HOOD = 0;
    public static double P_POS_HOOD = 0;
    public static double I_POS_HOOD = 0;
    public static double D_POS_HOOD = 0;

    public static double F_VEL_HOOD = 0;
    public static double P_VEL_HOOD = 0;
    public static double I_VEL_HOOD = 0;
    public static double D_VEL_HOOD = 0;

    public static final int PEAK_CURRENT_HOOD = 60;
    public static final int CONTINUOUS_CURRENT_LIMIT_HOOD = 40;

    public static final NeutralMode NEUTRAL_MODE_HOOD = NeutralMode.Brake;
    private AngularSpeed speedSetPointHood = AngularSpeed.ZERO;

    public static Angle setAngleHood = Angle.ZERO;
    public static Angle deltaAngleHood = Angle.ZERO;
    public static Angle goalAngleHood = Angle.ZERO;

    public static final int MOTION_MAGIC_SLOT = 2;

    public static final AngularAcceleration MAX_ACCEL = new AngularAcceleration(580, Angle.Unit.DEGREE, Time.Unit.SECOND, Time.Unit.SECOND);
	public static final AngularSpeed MAX_SPEED = new AngularSpeed(80, Angle.Unit.DEGREE, Time.Unit.SECOND);

    public static final int PID_TYPE = 0;

    public static final int VEL_SLOT = 0;
    public static final int POS_SLOT = 1;



    private Hood(){

        hoodTalon = CTRECreator.createMasterTalon(RobotMap.HOOD_TALON);

        hoodTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PID_TYPE, DEFAULT_TIMEOUT);


        hoodTalon.config_kF(VEL_SLOT, 0, DEFAULT_TIMEOUT);
        hoodTalon.config_kP(VEL_SLOT, P_VEL_HOOD, DEFAULT_TIMEOUT);
        hoodTalon.config_kI(VEL_SLOT, I_VEL_HOOD, DEFAULT_TIMEOUT);
        hoodTalon.config_kD(VEL_SLOT, D_VEL_HOOD, DEFAULT_TIMEOUT);
        hoodTalon.config_kF(MOTION_MAGIC_SLOT, F_POS_HOOD, DEFAULT_TIMEOUT);
        hoodTalon.config_kP(MOTION_MAGIC_SLOT, P_POS_HOOD, DEFAULT_TIMEOUT);
        hoodTalon.config_kI(MOTION_MAGIC_SLOT, I_POS_HOOD, DEFAULT_TIMEOUT);
        hoodTalon.config_kD(MOTION_MAGIC_SLOT, D_POS_HOOD, DEFAULT_TIMEOUT);

        hoodTalon.config_kF(POS_SLOT, F_POS_HOOD, DEFAULT_TIMEOUT);
        hoodTalon.config_kP(POS_SLOT, P_POS_HOOD, DEFAULT_TIMEOUT);
        hoodTalon.config_kI(POS_SLOT, I_POS_HOOD, DEFAULT_TIMEOUT);
        hoodTalon.config_kD(POS_SLOT, D_POS_HOOD, DEFAULT_TIMEOUT);

        hoodTalon.setNeutralMode(NEUTRAL_MODE_HOOD);

        hoodTalon.setInverted(false);
        //Change to Talon Version
        hoodTalon.setSensorPhase(false);

        hoodTalon.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_HOOD);

        hoodTalon.enableVoltageCompensation(true);
        hoodTalon.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);
        
        hoodTalon.enableCurrentLimit(true);
        hoodTalon.configPeakCurrentLimit(PEAK_CURRENT_HOOD, DEFAULT_TIMEOUT);
        hoodTalon.configPeakCurrentDuration(CONTINUOUS_CURRENT_LIMIT_HOOD, DEFAULT_TIMEOUT);

        hoodTalon.configClosedloopRamp(VOLTAGE_RAMP_RATE_HOOD.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
        hoodTalon.configOpenloopRamp(VOLTAGE_RAMP_RATE_HOOD.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);

        //Change Magnetic_encoder_tick_aux_drive to angle stuff, angular acc and velocity
        hoodTalon.configMotionCruiseVelocity((int) MAX_SPEED.mul(PROFILE_VEL_PERCENT_HOOD).get(Angle.Unit.HOOD_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND), DEFAULT_TIMEOUT);
        hoodTalon.configMotionAcceleration((int) MAX_ACCEL.mul(PROFILE_ACCEL_PERCENT_HOOD).get(Angle.Unit.HOOD_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND, Time.Unit.HUNDRED_MILLISECOND), DEFAULT_TIMEOUT);

        hoodTalon.getSensorCollection().setQuadraturePosition(0, DEFAULT_TIMEOUT);
    }

}