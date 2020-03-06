package edu.nr.robotics.subsystems.winch;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.motorcontrollers.CTRECreator;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Distance.Unit;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Winch extends NRSubsystem
{
    private static Winch singleton;

    private TalonSRX winchTalon;

    private double F_POS_WINCH = 0;
    private double P_POS_WINCH = 0;
    private double I_POS_WINCH = 0;
    private double D_POS_WINCH = 0;

    private double kFVelWinch = 0;
    private double kPVelWinch = 0;
    private double kIVelWinch = 0;
    private double kDVelWinch = 0;

    private static final int POS_SLOT = 0;
    private static final int VEL_SLOT = 1;

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

    public static final double WINCH_PERCENT = 0.3;

    public Winch()
    {
        winchTalon = CTRECreator.createMasterTalon(RobotMap.WINCH_TALON);
        winchTalon.config_kF(POS_SLOT, F_POS_WINCH);
        winchTalon.config_kP(POS_SLOT, P_POS_WINCH);
        winchTalon.config_kI(POS_SLOT, I_POS_WINCH);
        winchTalon.config_kD(POS_SLOT, D_POS_WINCH);

        winchTalon.config_kF(VEL_SLOT, kFVelWinch);
        winchTalon.config_kP(VEL_SLOT, kPVelWinch);
        winchTalon.config_kI(VEL_SLOT, kIVelWinch);
        winchTalon.config_kD(VEL_SLOT, kDVelWinch);

        winchTalon.enableCurrentLimit(true);
        winchTalon.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_WINCH);
        winchTalon.configPeakCurrentLimit(PEAK_CURRENT_WINCH);

        winchTalon.setNeutralMode(NeutralMode.Brake);

        winchTalon.getSensorCollection().setQuadraturePosition(0, DEFAULT_TIMEOUT);

        if(EnabledSubsystems.WINCH_DUMB_ENABLED){
            winchTalon.set(ControlMode.PercentOutput, 0);
        }else{
            winchTalon.set(ControlMode.Velocity, 0);
        }

    }

    public synchronized static void init()
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
        if(winchTalon != null)
            return new Distance(winchTalon.getSelectedSensorPosition(), Distance.Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY);
        return Distance.ZERO;
    }

    public void setPosition(Distance distance)
    {
        if(winchTalon != null)
        {
            winchTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
            winchTalon.set(ControlMode.Position, distance.get(Distance.Unit.INCH));
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

            SmartDashboard.putNumber("F Vel Winch", kFVelWinch);
            SmartDashboard.putNumber("P Vel Winch", kPVelWinch);
            SmartDashboard.putNumber("I Vel Winch", kIVelWinch);
            SmartDashboard.putNumber("D Vel Winch", kDVelWinch);

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

            kFVelWinch = SmartDashboard.getNumber("F Vel Winch", kFVelWinch);
            kPVelWinch = SmartDashboard.getNumber("P Vel Winch", kPVelWinch);
            kIVelWinch = SmartDashboard.getNumber("I Vel Winch", kIVelWinch);
            kDVelWinch = SmartDashboard.getNumber("D Vel Winch", kDVelWinch);

            winchTalon.config_kF(POS_SLOT, F_POS_WINCH);
            winchTalon.config_kP(POS_SLOT, P_POS_WINCH);
            winchTalon.config_kI(POS_SLOT, I_POS_WINCH);
            winchTalon.config_kD(POS_SLOT, D_POS_WINCH);

            winchTalon.config_kF(VEL_SLOT, kFVelWinch);
            winchTalon.config_kP(VEL_SLOT, kPVelWinch);
            winchTalon.config_kI(VEL_SLOT, kIVelWinch);
            winchTalon.config_kD(VEL_SLOT, kDVelWinch);
        }
    }

    public void setMotorSpeedRaw(double percent)
    {
        if(winchTalon != null)
        {
            winchTalon.set(ControlMode.PercentOutput, percent);
        }
    }

    public void setMotorSpeed(Speed speed)
    {
        if(winchTalon != null)
            winchTalon.set(ControlMode.Velocity, speed.get(Distance.Unit.MAGNETIC_ENCODER_TICK_WINCH, Time.Unit.HUNDRED_MILLISECOND));
    }

    public void disable()
    {
        if(winchTalon != null)
            setMotorSpeedRaw(0);
    }
}