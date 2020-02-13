package edu.nr.robotics.subsystems.winch;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.motorcontrollers.CTRECreator;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Distance.Unit;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Winch extends NRSubsystem
{
    private static Winch singleton;

    private TalonSRX WinchTalon;

    private double F_POS_WINCH = 0;
    private double P_POS_WINCH = 0;
    private double I_POS_WINCH = 0;
    private double D_POS_WINCH = 0;

    private static final int POS_SLOT = 0;

    private static Distance setPositionClimb;
    public static Distance goalPositionClimb;

    private NeutralMode NEUTRAL_MODE_CLIMB_DEPLOY = NeutralMode.Brake;

    private static Time VOLTAGE_RAMP_RATE_WINCH = new Time(0.05, Time.Unit.SECOND);
    private static final int VOLTAGE_COMPENSATION_LEVEL = 12;
    private static final double MIN_MOVE_VOLTAGE = 0.0;
    private static final int DEFAULT_TIMEOUT = 0;

    private static final int PEAK_CURRENT_WINCH = 60;
    private static final int CONTINUOUS_CURRENT_LIMIT_WINCH = 40;

    public static final double ENCODER_TICKS_PER_INCH_WINCH = 0;

    public static final double WINCH_PERCENT = 0.0;

    public Winch()
    {
        WinchTalon = CTRECreator.createMasterTalon(RobotMap.WINCH_TALON);
        WinchTalon.config_kF(POS_SLOT, F_POS_WINCH);
        WinchTalon.config_kP(POS_SLOT, P_POS_WINCH);
        WinchTalon.config_kI(POS_SLOT, I_POS_WINCH);
        WinchTalon.config_kD(POS_SLOT, D_POS_WINCH);

        WinchTalon.enableCurrentLimit(true);
        WinchTalon.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_WINCH);
        WinchTalon.configPeakCurrentLimit(PEAK_CURRENT_WINCH);

        WinchTalon.setNeutralMode(NeutralMode.Brake);

        WinchTalon.getSensorCollection().setQuadraturePosition(0, DEFAULT_TIMEOUT);

        if(EnabledSubsystems.WINCH_DUMB_ENABLED){
            WinchTalon.set(ControlMode.PercentOutput, 0);
        }else{
            WinchTalon.set(ControlMode.Velocity, 0);
        }

    }

    public static void init()
    {
        if(singleton == null)
        {
            singleton = new Winch();
        }
    }

    public static Winch getInstance()
    {
        if(singleton == null)
        {
            init();
        }
        return singleton;
    }

    public Distance getPosition()
    {
        if(WinchTalon != null)
            return new Distance(WinchTalon.getSelectedSensorPosition(), Distance.Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY);
        return Distance.ZERO;
    }

    public void setPosition(Distance distance)
    {
        if(WinchTalon != null)
        {
            WinchTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
            WinchTalon.set(ControlMode.Position, distance.get(Distance.Unit.INCH));
        }
    }

    public void smartDashboardInit()
    {
        if(EnabledSubsystems.WINCH_SMARTDASHBOARD_DEBUG_ENABLED)
        {
            SmartDashboard.putNumber("Set Distance Winch: ", setPositionClimb.get(Distance.Unit.INCH));
            
            SmartDashboard.putNumber("F_POS_WINCH", F_POS_WINCH);
            SmartDashboard.putNumber("P_POS_WINCH", P_POS_WINCH);
            SmartDashboard.putNumber("I_POS_WINCH", I_POS_WINCH);
            SmartDashboard.putNumber("D_POS_WINCH", D_POS_WINCH);

            SmartDashboard.putNumber("Current Winch Position", getPosition().get(Unit.MAGNETIC_ENCODER_TICK_WINCH));

            SmartDashboard.putNumber("Goal Position Winch", goalPositionClimb.get(Distance.Unit.INCH));
        }
    }

    public void smartDashboardInfo()
    {
        if(EnabledSubsystems.WINCH_SMARTDASHBOARD_DEBUG_ENABLED)
        {
            //maybe make the 0 a goalposclimb get inches...
            goalPositionClimb = new Distance(SmartDashboard.getNumber("Goal Position Winch", 0), Distance.Unit.INCH);

            F_POS_WINCH = SmartDashboard.getNumber("F_POS_WINCH", F_POS_WINCH);
            P_POS_WINCH = SmartDashboard.getNumber("P_POS_WINCH", P_POS_WINCH);
            I_POS_WINCH = SmartDashboard.getNumber("I_POS_WINCH", I_POS_WINCH);
            D_POS_WINCH = SmartDashboard.getNumber("D_POS_WINCH", D_POS_WINCH);
        }
    }

    public void setMotorSpeedRaw(double percent)
    {
        if(WinchTalon != null)
        {
            WinchTalon.set(ControlMode.PercentOutput, percent);
        }
    }

    public void disable()
    {

    }
}