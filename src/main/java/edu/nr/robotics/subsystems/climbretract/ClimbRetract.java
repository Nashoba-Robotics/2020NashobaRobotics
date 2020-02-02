package edu.nr.robotics.subsystems.climbretract;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.motorcontrollers.CTRECreator;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Distance.Unit;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ClimbRetract extends NRCommand
{
    private static ClimbRetract singleton;

    private TalonSRX ClimbRetractTalon;

    private double F_POS_CLIMB_RETRACT = 0;
    private double P_POS_CLIMB_RETRACT = 0;
    private double I_POS_CLIMB_RETRACT = 0;
    private double D_POS_CLIMB_RETRACT = 0;

    private static final int POS_SLOT = 0;

    private static Distance setPositionClimb;
    private static Distance goalPositionClimb;

    private NeutralMode NEUTRAL_MODE_CLIMB_DEPLOY = NeutralMode.Brake;

    private static Time VOLTAGE_RAMP_RATE_CLIMB_RETRACT = new Time(0.05, Time.Unit.SECOND);
    private static final int VOLTAGE_COMPENSATION_LEVEL = 12;
    private static final double MIN_MOVE_VOLTAGE = 0.0;
    private static final int DEFAULT_TIMEOUT = 0;

    private static final int PEAK_CURRENT_CLIMB_RETRACT = 60;
    private static final int CONTINUOUS_CURRENT_LIMIT_CLIMB_RETRACT = 40;

    public static final double ENCODER_TICKS_PER_INCH_CLIMB_RETRACT = 0;

    public ClimbRetract()
    {
        ClimbRetractTalon = CTRECreator.createMasterTalon(RobotMap.CLIMB_RETRACT_TALON);
        ClimbRetractTalon.config_kF(POS_SLOT, F_POS_CLIMB_RETRACT);
        ClimbRetractTalon.config_kP(POS_SLOT, P_POS_CLIMB_RETRACT);
        ClimbRetractTalon.config_kI(POS_SLOT, I_POS_CLIMB_RETRACT);
        ClimbRetractTalon.config_kD(POS_SLOT, D_POS_CLIMB_RETRACT);

        ClimbRetractTalon.enableCurrentLimit(true);
        ClimbRetractTalon.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_CLIMB_RETRACT);
        ClimbRetractTalon.configPeakCurrentLimit(PEAK_CURRENT_CLIMB_RETRACT);

        ClimbRetractTalon.setNeutralMode(NeutralMode.Brake);

        ClimbRetractTalon.getSensorCollection().setQuadraturePosition(0, DEFAULT_TIMEOUT);

        if(EnabledSubsystems.CLIMB_RETRACT_DUMB_ENABLED){
            ClimbRetractTalon.set(ControlMode.PercentOutput, 0);
        }else{
            ClimbRetractTalon.set(ControlMode.Velocity, 0);
        }

    }

    public void init()
    {
        if(singleton == null)
        {
            singleton = new ClimbRetract();
        }
    }

    public ClimbRetract getInstance()
    {
        if(singleton == null)
        {
            init();
        }
        return singleton;
    }

    public Distance getPosition()
    {
        if(ClimbRetractTalon != null)
            return new Distance(ClimbRetractTalon.getSelectedSensorPosition(), Distance.Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY);
        return Distance.ZERO;
    }

    public void setPosition(Distance distance)
    {
        if(ClimbRetractTalon != null)
        {
            ClimbRetractTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
            ClimbRetractTalon.set(ControlMode.Position, distance.get(Distance.Unit.INCH));
        }
    }

    public void SmartDashboardInit()
    {
        if(EnabledSubsystems.CLIMB_RETRACT_SMARTDASHBOARD_DEBUG_ENABLED)
        {
            SmartDashboard.putNumber("Set Distance Climb Retract: ", setPositionClimb.get(Distance.Unit.INCH));
            
            SmartDashboard.putNumber("F_POS_CLIMB_RETRACT", F_POS_CLIMB_RETRACT);
            SmartDashboard.putNumber("P_POS_CLIMB_RETRACT", P_POS_CLIMB_RETRACT);
            SmartDashboard.putNumber("I_POS_CLIMB_RETRACT", I_POS_CLIMB_RETRACT);
            SmartDashboard.putNumber("D_POS_CLIMB_RETRACT", D_POS_CLIMB_RETRACT);

            SmartDashboard.putNumber("Current Climb Retract Position", getPosition().get(Unit.MAGNETIC_ENCODER_TICK_CLIMB_RETRACT));

            SmartDashboard.putNumber("Goal Position Climb Retract", goalPositionClimb.get(Distance.Unit.INCH));
        }
    }

    public void SmartDashboardInfo()
    {
        if(EnabledSubsystems.CLIMB_RETRACT_SMARTDASHBOARD_DEBUG_ENABLED)
        {
            //maybe make the 0 a goalposclimb get inches...
            goalPositionClimb = new Distance(SmartDashboard.getNumber("Goal Position Climb Retract", 0), Distance.Unit.INCH);

            F_POS_CLIMB_RETRACT = SmartDashboard.getNumber("F_POS_CLIMB_RETRACT", F_POS_CLIMB_RETRACT);
            P_POS_CLIMB_RETRACT = SmartDashboard.getNumber("P_POS_CLIMB_RETRACT", P_POS_CLIMB_RETRACT);
            I_POS_CLIMB_RETRACT = SmartDashboard.getNumber("I_POS_CLIMB_RETRACT", I_POS_CLIMB_RETRACT);
            D_POS_CLIMB_RETRACT = SmartDashboard.getNumber("D_POS_CLIMB_RETRACT", D_POS_CLIMB_RETRACT);
        }
    }
}