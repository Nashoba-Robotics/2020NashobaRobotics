package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.motorcontrollers.CTRECreator;
import edu.nr.lib.units.Acceleration;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Indexer extends NRSubsystem {

    private static Indexer singleton;

    private TalonSRX indexerTalon;
    

    public static final int INDEXER_INPUT_THRESHOLD = 0;
    public static final int INDEXER_SPACING_CLOSE_THRESHOLD = 0;
    public static final int INDEXER_SPACING_FAR_THRESHOLD = 0;
    public static final int INDEXER_READY_SHOT_THRESHOLD = 0;

    public static final double ENCODER_TICKS_PER_DEGREE = 2048 / 360;
    public static final double ENCODER_TICKS_PER_INCH_BALL_MOVED = 0;

    public static final Speed MAX_SPEED_INDEXER = new Speed(1, Distance.Unit.METER, Time.Unit.SECOND);
    public static final Acceleration MAX_ACCELERATION_INDEXER = new Acceleration(1, Distance.Unit.METER, Time.Unit.SECOND, Time.Unit.SECOND);

    public static Time VOLTAGE_RAMP_RATE_INDEXER = new Time(0.05, Time.Unit.SECOND);
    public static final int VOLTAGE_COMPENSATION_LEVEL = 12;
    public static final double MIN_MOVE_VOLTAGE = 0.0;
    public static final int DEFAULT_TIMEOUT = 0;

    public static double F_POS_INDEXER = 0;
    public static double P_POS_INDEXER = 0;
    public static double I_POS_INDEXER = 0;
    public static double D_POS_INDEXER = 0;

    public static final double PROFILE_VEL_PERCENT_INDEXER = 0.6; //change
    public static final double PROFILE_ACCEL_PERCENT_INDEXER = 0.6;

    public static final int PEAK_CURRENT_INDEXER = 60;
    public static final int CONTINUOUS_CURRENT_LIMIT_INDEXER = 40;

    public static final NeutralMode NEUTRAL_MODE_INDEXER = NeutralMode.Brake;

    public static final int PID_TYPE = 0;

    public static final int VEL_SLOT = 0;
    public static final int POS_SLOT = 1;

    public static final int MOTION_MAGIC_SLOT = 2;

    public static Speed speedSetPoint = Speed.ZERO;
    public static Speed goalSpeed = Speed.ZERO;

    public static Distance distanceSetPoint = Distance.ZERO;
    public static Distance DeltaPosition = Distance.ZERO;
    public static final Distance SHOOT_ONE_DISTANCE = Distance.ZERO;

    private Indexer(){
        if(EnabledSubsystems.INDEXER_ENABLED){
            indexerTalon = CTRECreator.createMasterTalon(RobotMap.INDEXER_TALON);

            indexerTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PID_TYPE, DEFAULT_TIMEOUT);

            indexerTalon.config_kF(MOTION_MAGIC_SLOT, F_POS_INDEXER, DEFAULT_TIMEOUT);
            indexerTalon.config_kP(MOTION_MAGIC_SLOT, P_POS_INDEXER, DEFAULT_TIMEOUT);
            indexerTalon.config_kI(MOTION_MAGIC_SLOT, I_POS_INDEXER, DEFAULT_TIMEOUT);
            indexerTalon.config_kD(MOTION_MAGIC_SLOT, D_POS_INDEXER, DEFAULT_TIMEOUT);

            indexerTalon.config_kF(POS_SLOT, F_POS_INDEXER, DEFAULT_TIMEOUT);
            indexerTalon.config_kP(POS_SLOT, P_POS_INDEXER, DEFAULT_TIMEOUT);
            indexerTalon.config_kI(POS_SLOT, I_POS_INDEXER, DEFAULT_TIMEOUT);
            indexerTalon.config_kD(POS_SLOT, D_POS_INDEXER, DEFAULT_TIMEOUT);

            indexerTalon.setNeutralMode(NEUTRAL_MODE_INDEXER);

            indexerTalon.setInverted(false);
            //Change to Talon Version
            indexerTalon.setSensorPhase(false);

            indexerTalon.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_INDEXER);

            indexerTalon.enableVoltageCompensation(true);
            indexerTalon.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);
            
            indexerTalon.enableCurrentLimit(true);
            indexerTalon.configPeakCurrentLimit(PEAK_CURRENT_INDEXER, DEFAULT_TIMEOUT);
            indexerTalon.configPeakCurrentDuration(CONTINUOUS_CURRENT_LIMIT_INDEXER, DEFAULT_TIMEOUT);

            indexerTalon.configClosedloopRamp(VOLTAGE_RAMP_RATE_INDEXER.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
            indexerTalon.configOpenloopRamp(VOLTAGE_RAMP_RATE_INDEXER.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);

            indexerTalon.configMotionCruiseVelocity((int) MAX_SPEED_INDEXER.mul(PROFILE_VEL_PERCENT_INDEXER).get(Distance.Unit.METER, Time.Unit.HUNDRED_MILLISECOND), DEFAULT_TIMEOUT);
            indexerTalon.configMotionAcceleration((int) MAX_ACCELERATION_INDEXER.mul(PROFILE_ACCEL_PERCENT_INDEXER)
                    .get(Distance.Unit.METER, Time.Unit.SECOND, Time.Unit.SECOND));

            indexerTalon.getSensorCollection().setQuadraturePosition(0, DEFAULT_TIMEOUT);

            if(EnabledSubsystems.INDEXER_DUMB_ENABLED){
                indexerTalon.set(ControlMode.PercentOutput, 0);
            }else{
                indexerTalon.set(ControlMode.Velocity, 0);
            }

        }
        smartDashboardInit();
    }

    public synchronized static void init(){
        if(singleton == null){
            singleton = new Indexer();
        }
    }

    public static Indexer getInstance(){
        if(singleton == null) {
            singleton = new Indexer();
        }
        return singleton;
    }

    public void disable(){
        if(indexerTalon != null)
        indexerTalon.set(ControlMode.PercentOutput,0);
        //setposition to current position
    }

    public void smartDashboardInit(){

    }

    public void smartDashboardInfo(){
        if(EnabledSubsystems.INDEXER_SMARTDASHBOARD_DEBUG_ENABLED){
            SmartDashboard.putNumber("Indexer Delta Position", DeltaPosition.get(Distance.Unit.INCH));
            SmartDashboard.putNumber("Indexer Goal Speed", goalSpeed.get(Distance.Unit.INCH, Time.Unit.SECOND));

            goalSpeed = new Speed(SmartDashboard.getNumber("Indexer Goal Speed", goalSpeed.get(Distance.Unit.INCH, Time.Unit.SECOND)), Distance.Unit.INCH, Time.Unit.SECOND);
        }
    }
    
    public Distance getPosition(){
        if(indexerTalon != null){
            return new Distance (indexerTalon.getSelectedSensorPosition(), Distance.Unit.MAGNETIC_ENCODER_TICK_INDEXER);
        }
        return Distance.ZERO;
    }

    public void setMotorSpeedInPercent(double percent){
        if(indexerTalon != null){
            indexerTalon.set(ControlMode.PercentOutput, percent);
        }
    }

    public Speed getSpeed(){
        if(indexerTalon != null){
            return new Speed(indexerTalon.getSelectedSensorVelocity(), Distance.Unit.MAGNETIC_ENCODER_TICK_INDEXER, Time.Unit.HUNDRED_MILLISECOND);
        }
        return Speed.ZERO;
    }

    public boolean [] sensors(){
        return new boolean [] {EnabledSensors.IndexerInput.get(), EnabledSensors.IndexerSpacingClose.get(), EnabledSensors.IndexerSpacingFar.get(), EnabledSensors.IndexerReadyShot.get()}; // change to hold sensor values
    }

    public void setPosition(Distance position){
        distanceSetPoint = position;
        if(indexerTalon != null){
        indexerTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
        indexerTalon.set(ControlMode.Position, distanceSetPoint.get(Distance.Unit.MAGNETIC_ENCODER_TICK_INDEXER));
        }
    }

    public void setSpeed(Speed target){
        speedSetPoint = target;
        
        if(indexerTalon != null){
            indexerTalon.selectProfileSlot(VEL_SLOT, DEFAULT_TIMEOUT);
            indexerTalon.set(ControlMode.Velocity, target.get(Distance.Unit.MAGNETIC_ENCODER_TICK_INDEXER, Time.Unit.HUNDRED_MILLISECOND));
        }
    }

    public double getCurrent(){
        if(indexerTalon != null){
            return indexerTalon.getStatorCurrent();
        }
        return 0;
    }

}