package edu.nr.robotics.subsystems.arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;

import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Acceleration;

public class Arm extends NRSubsystem
{
    private Arm singleton;

    private TalonSRX armTalon;

    public static final double ENCODER_TICKS_PER_INCH_ARM = 0;

    public static final Speed MAX_ARM_SPEED_UP = new Speed(0, Distance.Unit.FOOT, Time.Unit.SECOND);
    public static final Speed MAX_ARM_SPEED_DOWN = new Speed(0, Distance.Unit.FOOT, Time.Unit.SECOND);

    public static final Acceleration MAX_ARM_ACCELERATION_UP = new Acceleration(0, Distance.Unit.FOOT, Time.Unit.SECOND, Time.Unit.SECOND);
    public static final Acceleration MAX_ARM_ACCELERATION_DOWN = new Acceleration(0, Distance.Unit.FOOT, Time.Unit.SECOND, Time.Unit.SECOND);

    public static final Speed MAX_CLIMB_SPEED = Speed.ZERO;

    public static final Speed MAX_SPEED_EP = Speed.ZERO;

    public static final Acceleration MAX_CLIMB_ACCEL_UP = Acceleration.ZERO;
    public static final Acceleration MAX_CLIMB_ACCEL_DOWN = Acceleration.ZERO;

    public static final double F_POS_ARM = 0.0;
    public static final double P_POS_ARM = 0.0;
    public static final double I_POS_ARM = 0.0;
    public static final double D_POS_ARM = 0.0;

    public static final double F_POS_ARM_HOLD = 0.0;
    public static final double P_POS_ARM_HOLD = 0.0;
    public static final double I_POS_ARM_HOLD = 0.0;
    public static final double D_POS_ARM_HOLD = 0.0;

    public static final int POS_SLOT = 0;

    public static final int DEFAULT_TIMEOUT = 0;

    public static final NeutralMode NEUTRAL_MODE_ELEVATOR = NeutralMode.Brake;

    

    public Arm()
    {
        super();
        armTalon = new TalonSRX(RobotMap.ARM_TALON);
    }

    public void init()
    {
        if(singleton == null)
            singleton = new Arm();
    }

    public Arm getInstance()
    {
        if(singleton == null)
            init();
        return singleton; 
    }

    public void disable()
    {
        if(armTalon != null){
            armTalon.set(ControlMode.PercentOutput, 0);
        }
    }

    public void smartDashboardInit()
    {

    }

    public void smartDashboardInfo()
    {

    }
}