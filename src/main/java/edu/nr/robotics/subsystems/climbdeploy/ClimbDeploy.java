package edu.nr.robotics.subsystems.climbdeploy;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
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

public class ClimbDeploy extends NRSubsystem{

    private static ClimbDeploy singleton;

    private TalonSRX climbDeployTalon;

    public static final double ENCODER_TICKS_PER_INCH_CLIMB_DEPLOY = 0;

    public static double F_POS_CLIMB_DEPLOY = 0;
    public static double P_POS_CLIMB_DEPLOY = 0;
    public static double I_POS_CLIMB_DEPLOY = 0;
    public static double D_POS_CLIMB_DEPLOY = 0;

    public static Time VOLTAGE_RAMP_RATE_CLIMB_DEPLOY = new Time(0.05, Time.Unit.SECOND);
    public static final int VOLTAGE_COMPENSATION_LEVEL = 12;
    public static final double MIN_MOVE_VOLTAGE = 0.0;
    public static final int DEFAULT_TIMEOUT = 0;

    public static final int PEAK_CURRENT_CLIMB_DEPLOY = 60;
    public static final int CONTINUOUS_CURRENT_LIMIT_CLIMB_DEPLOY = 40;

    public static final Distance MINIMUM = Distance.ZERO;
    public static final Distance MAXIMUM = new Distance(24, Distance.Unit.INCH);

    NeutralMode NEUTRAL_MODE_CLIMB_DEPLOY = NeutralMode.Brake;

    public static final int POS_SLOT = 0;
    public static final int VEL_SLOT = 1;

    public static Distance setPositionClimb;
    public static Distance goalPositionClimb;

    public static final Distance DEPLOY_DISTANCE = Distance.ZERO;


    private ClimbDeploy(){
        if(EnabledSubsystems.CLIMB_DEPLOY_ENABLED){

            climbDeployTalon = CTRECreator.createMasterTalon(RobotMap.CLIMB_DEPLOY_TALON);

            climbDeployTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

            climbDeployTalon.config_kF(POS_SLOT, F_POS_CLIMB_DEPLOY);
            climbDeployTalon.config_kP(POS_SLOT, P_POS_CLIMB_DEPLOY);
            climbDeployTalon.config_kI(POS_SLOT, I_POS_CLIMB_DEPLOY);
            climbDeployTalon.config_kD(POS_SLOT, D_POS_CLIMB_DEPLOY);

            climbDeployTalon.setNeutralMode(NEUTRAL_MODE_CLIMB_DEPLOY);

            climbDeployTalon.setSensorPhase(false);

            climbDeployTalon.enableCurrentLimit(true);
            climbDeployTalon.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_CLIMB_DEPLOY);
            climbDeployTalon.configPeakCurrentLimit(PEAK_CURRENT_CLIMB_DEPLOY);

            climbDeployTalon.enableVoltageCompensation(true);
            climbDeployTalon.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);

            climbDeployTalon.configClosedloopRamp(VOLTAGE_RAMP_RATE_CLIMB_DEPLOY.get(Time.Unit.SECOND));
            climbDeployTalon.configOpenloopRamp(VOLTAGE_RAMP_RATE_CLIMB_DEPLOY.get(Time.Unit.SECOND));

            climbDeployTalon.getSensorCollection().setQuadraturePosition(0, DEFAULT_TIMEOUT);

            if(EnabledSubsystems.CLIMB_DEPLOY_DUMB_ENABLED){
                climbDeployTalon.set(ControlMode.PercentOutput, 0);
            }else{
                climbDeployTalon.set(ControlMode.Velocity, 0);
            }
        }
        SmartDashboardInit();

    }

    public synchronized static void init(){
        if(singleton == null){
            singleton = new ClimbDeploy();
        }
    }

    public static ClimbDeploy getInstance(){
        if(singleton == null){
            init();
        }
        return singleton;
    }

    public void disable(){
        if(climbDeployTalon != null){
            climbDeployTalon.set(ControlMode.PercentOutput, 0);
        }
    }

    public void SmartDashboardInit(){
        if(EnabledSubsystems.CLIMB_DEPLOY_SMARTDASHBOARD_DEBUG_ENABLED){
            SmartDashboard.putNumber("F_POS_CLIMB_DEPLOY", F_POS_CLIMB_DEPLOY);
            SmartDashboard.putNumber("P_POS_CLIMB_DEPLOY", P_POS_CLIMB_DEPLOY);
            SmartDashboard.putNumber("I_POS_CLIMB_DEPLOY", I_POS_CLIMB_DEPLOY);
            SmartDashboard.putNumber("D_POS_CLIMB_DEPLOY", D_POS_CLIMB_DEPLOY);

            SmartDashboard.putNumber("Set Distance Climb Deploy", DEPLOY_DISTANCE.get(Distance.Unit.INCH));

            SmartDashboard.putNumber("Climb Deploy Position", getPosition().get(Distance.Unit.INCH));
            SmartDashboard.putNumber("Climb Deploy Current", climbDeployTalon.getStatorCurrent());
            SmartDashboard.putNumber("Goal Angle Climb Deploy: ", ClimbDeploy.goalPositionClimb.get(Distance.Unit.INCH));
        }
    }

    public Distance getPosition(){
        if(climbDeployTalon != null){
            return new Distance(climbDeployTalon.getSelectedSensorPosition(), Distance.Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY);
        }
        return Distance.ZERO;
    }

    public void setPosition(Distance distance)
    {
        if(climbDeployTalon != null)
        {
            climbDeployTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
            climbDeployTalon.set(ControlMode.Position, distance.get(Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY));
        }
    }

    public void Deploy()
    {
        if(climbDeployTalon != null)
        {
            climbDeployTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
            climbDeployTalon.set(ControlMode.Position, DEPLOY_DISTANCE.get(Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY));
        }
    }

    public void setMotorSpeedRaw(double percent)
    {
        if(climbDeployTalon != null)
        {
            climbDeployTalon.selectProfileSlot(VEL_SLOT, DEFAULT_TIMEOUT);
            climbDeployTalon.set(ControlMode.Velocity, percent);
        }
    }

    public void smartDashboardInfo(){
        if(climbDeployTalon != null){

            if(EnabledSubsystems.CLIMB_DEPLOY_SMARTDASHBOARD_DEBUG_ENABLED){
                SmartDashboard.putNumber("F_POS_CLIMB_DEPLOY", F_POS_CLIMB_DEPLOY);
                SmartDashboard.putNumber("P_POS_CLIMB_DEPLOY", P_POS_CLIMB_DEPLOY);
                SmartDashboard.putNumber("I_POS_CLIMB_DEPLOY", I_POS_CLIMB_DEPLOY);
                SmartDashboard.putNumber("D_POS_CLIMB_DEPLOY", D_POS_CLIMB_DEPLOY);

                SmartDashboard.putNumber("Set Distance Climb Deploy", DEPLOY_DISTANCE.get(Distance.Unit.INCH));

                SmartDashboard.putNumber("Climb Deploy Position", getPosition().get(Distance.Unit.INCH));
                SmartDashboard.putNumber("Climb Deploy Current", climbDeployTalon.getStatorCurrent());

                goalPositionClimb = new Distance(SmartDashboard.getNumber("Goal Angle Climb Deploy", 0), Distance.Unit.INCH);
            }
        }
    }
}