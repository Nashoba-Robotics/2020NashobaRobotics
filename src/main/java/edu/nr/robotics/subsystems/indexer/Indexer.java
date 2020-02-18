package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.motorcontrollers.CTRECreator;
import edu.nr.lib.units.Acceleration;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Distance.Unit;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.multicommands.CanWeIndexCommand;
import edu.nr.robotics.multicommands.IndexingProcedureCommand;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.AnalogSensor;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jdk.jfr.Threshold;
//import jdk.jfr.internal.settings.ThresholdSetting;

import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
 
public class Indexer extends NRSubsystem {
 
    private static Indexer singleton;
 
    private TalonFX indexerTalon;
    
    public static final int INDEXER_PUKE_SENSOR_THRESHOLD = 0; // needs to be final for real code in comp.
    public static final int INDEXER_SETTING1_THRESHOLD = 0;
    public static final int INDEXER_SETTING2_THRESHOLD = 0;
    public static final int INDEXER_SETTING3_THRESHOLD = 0;
    public static final int INDEXER_SHOOTER_SENSOR_THRESHOLD = 0;
    
    public static final double ENCODER_TICKS_PER_DEGREE = 2048 / 360;
    public static final double ENCODER_TICKS_PER_INCH_BALL_MOVED = 400;//not really, will have to change
 
    public static final Speed MAX_SPEED_INDEXER = new Speed(1, Distance.Unit.METER, Time.Unit.SECOND);
    public static final Acceleration MAX_ACCELERATION_INDEXER = new Acceleration(1, Distance.Unit.METER, Time.Unit.SECOND, Time.Unit.SECOND);
 
    public static Time VOLTAGE_RAMP_RATE_INDEXER = new Time(0.05, Time.Unit.SECOND);
    public static final int VOLTAGE_COMPENSATION_LEVEL = 12;//
    public static final double MIN_MOVE_VOLTAGE = 0.0;
    public static final int DEFAULT_TIMEOUT = 0;
 
    public static double F_POS_INDEXER = 0;
    public static double P_POS_INDEXER = 0;
    public static double I_POS_INDEXER = 0;
    public static double D_POS_INDEXER = 0;

    public static double F_VEL_INDEXER = 1;
    public static double P_VEL_INDEXER = 0.005;
    public static double I_VEL_INDEXER = 0;
    public static double D_VEL_INDEXER = 0;
 
    public static final double PROFILE_VEL_PERCENT_INDEXER = 0.6; //change
    public static final double PROFILE_ACCEL_PERCENT_INDEXER = 0.6;
 
    public static final int PEAK_CURRENT_INDEXER = 60;
    public static final int CONTINUOUS_CURRENT_LIMIT_INDEXER = 40;
 
    public static final NeutralMode NEUTRAL_MODE_INDEXER = NeutralMode.Brake;
 
    public static final int PID_TYPE = 0;
 
    public static final int VEL_SLOT = 0;
    public static final int POS_SLOT = 1;
 
    public static final int MOTION_MAGIC_SLOT = 2;

    public static final double PUKE_PERCENT = -1;
    public static final Time PUKE_TIME = new Time(1, Time.Unit.SECOND);
 
    public static Speed speedSetPoint = Speed.ZERO;
    public static Speed goalSpeed = Speed.ZERO;
 
    public static Distance distanceSetPoint = Distance.ZERO;
    public static Distance DeltaPosition = Distance.ZERO;
    public static final Distance SHOOT_ONE_DISTANCE = new Distance(0, Distance.Unit.INCH); // change for real robot, might just set velocity to shoot for real
 
    public static final Speed SHOOTING_SPEED = new Speed(0, Distance.Unit.FOOT, Time.Unit.SECOND);

    private Indexer(){
        if(EnabledSubsystems.INDEXER_ENABLED){
            indexerTalon = new TalonFX(RobotMap.INDEXER_TALON); //no ctrecreator
 
            indexerTalon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, PID_TYPE, DEFAULT_TIMEOUT);

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
 
        //    indexerTalon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration() ); // does nothing, look up eventually

            indexerTalon.enableVoltageCompensation(true);
            indexerTalon.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);
            
        //    indexerTalon.enableCurrentLimit(true);
        //    indexerTalon.configPeakCurrentLimit(PEAK_CURRENT_INDEXER, DEFAULT_TIMEOUT);
        //    indexerTalon.configPeakCurrentDuration(CONTINUOUS_CURRENT_LIMIT_INDEXER, DEFAULT_TIMEOUT);
 
            indexerTalon.configClosedloopRamp(VOLTAGE_RAMP_RATE_INDEXER.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
            indexerTalon.configOpenloopRamp(VOLTAGE_RAMP_RATE_INDEXER.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
 
            indexerTalon.configMotionCruiseVelocity((int) MAX_SPEED_INDEXER.mul(PROFILE_VEL_PERCENT_INDEXER).get(Distance.Unit.METER, Time.Unit.HUNDRED_MILLISECOND), DEFAULT_TIMEOUT);
            indexerTalon.configMotionAcceleration((int) MAX_ACCELERATION_INDEXER.mul(PROFILE_ACCEL_PERCENT_INDEXER)
                    .get(Distance.Unit.METER, Time.Unit.SECOND, Time.Unit.SECOND));
 
            indexerTalon.getSensorCollection().setIntegratedSensorPosition(0, DEFAULT_TIMEOUT);
 
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
        //    singleton.setDefaultCommand(new CanWeIndexCommand()); //this might be garbage
            singleton.setDefaultCommand(new IndexingProcedureCommand());
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
 
        if(EnabledSubsystems.INDEXER_SMARTDASHBOARD_DEBUG_ENABLED){
 
            SmartDashboard.putNumber("Sensor Threshold", 0);

            SmartDashboard.putNumber("Indexer Speed" , getSpeed().get(Distance.Unit.INCH, Time.Unit.SECOND));

            SmartDashboard.putNumber("Indexer Goal Speed", goalSpeed.get(Distance.Unit.INCH, Time.Unit.SECOND));

            SmartDashboard.putNumber("Indexer Position", getPosition().get(Distance.Unit.INCH));
            SmartDashboard.putNumber("Indexer Delta Position", DeltaPosition.get(Distance.Unit.INCH));
 
            SmartDashboard.putNumber("F_VEL_INDEXER", F_VEL_INDEXER);
            SmartDashboard.putNumber("P_VEL_INDEXER", P_VEL_INDEXER);
            SmartDashboard.putNumber("I_VEL_INDEXER", I_VEL_INDEXER);
            SmartDashboard.putNumber("D_VEL_INDEXER", D_VEL_INDEXER);

            SmartDashboard.putNumber("F_POS_INDEXER", F_POS_INDEXER);
            SmartDashboard.putNumber("P_POS_INDEXER", P_POS_INDEXER);
            SmartDashboard.putNumber("I_POS_INDEXER", I_POS_INDEXER);
            SmartDashboard.putNumber("D_POS_INDEXER", D_POS_INDEXER);

        
        }
    }
 
    public void smartDashboardInfo(){
        if(EnabledSubsystems.INDEXER_SMARTDASHBOARD_DEBUG_ENABLED){

            SmartDashboard.putNumber("Indexer Speed" , getSpeed().get(Distance.Unit.INCH, Time.Unit.SECOND));

            F_POS_INDEXER = SmartDashboard.getNumber("F_POS_INDEXER", F_POS_INDEXER);
            P_POS_INDEXER = SmartDashboard.getNumber("P_POS_INDEXER", P_POS_INDEXER);
            I_POS_INDEXER = SmartDashboard.getNumber("I_POS_INDEXER", I_POS_INDEXER);
            D_POS_INDEXER = SmartDashboard.getNumber("D_POS_INDEXER", D_POS_INDEXER);

            F_VEL_INDEXER = SmartDashboard.getNumber("F_VEL_INDEXER", F_VEL_INDEXER);
            P_VEL_INDEXER = SmartDashboard.getNumber("P_VEL_INDEXER", P_VEL_INDEXER);
            I_VEL_INDEXER = SmartDashboard.getNumber("I_VEL_INDEXER", I_VEL_INDEXER);
            D_VEL_INDEXER = SmartDashboard.getNumber("D_VEL_INDEXER", D_VEL_INDEXER);

            indexerTalon.config_kF(VEL_SLOT, F_VEL_INDEXER);
            indexerTalon.config_kP(VEL_SLOT, P_VEL_INDEXER);
            indexerTalon.config_kI(VEL_SLOT, I_VEL_INDEXER);
            indexerTalon.config_kD(VEL_SLOT, D_VEL_INDEXER);

            indexerTalon.config_kF(POS_SLOT, F_POS_INDEXER);
            indexerTalon.config_kP(POS_SLOT, P_POS_INDEXER);
            indexerTalon.config_kI(POS_SLOT, I_POS_INDEXER);
            indexerTalon.config_kD(POS_SLOT, D_POS_INDEXER);

            SmartDashboard.putNumber("Indexer Encoder Position", indexerTalon.getSelectedSensorPosition());

            DeltaPosition = new Distance (SmartDashboard.getNumber("Indexer Delta Position", DeltaPosition.get(Distance.Unit.INCH)), Distance.Unit.INCH);
            SmartDashboard.putNumber("Indexer Goal Speed", goalSpeed.get(Distance.Unit.INCH, Time.Unit.SECOND));
 
            //EnabledSensors.IndexerInput.setThreshold(INDEXER_INPUT_THRESHOLD);
 
            //INDEXER_INPUT_THRESHOLD = (int) SmartDashboard.getNumber("Sensor Threshold", INDEXER_INPUT_THRESHOLD);
            //SmartDashboard.putBoolean("Sensor Triggered", EnabledSensors.IndexerInput.get());
            //SmartDashboard.putNumber("Sensor value", EnabledSensors.IndexerInput.getSensor().getValue());

            SmartDashboard.putNumber("Indexer Delta Position", DeltaPosition.get(Distance.Unit.INCH));
            SmartDashboard.putNumber("Indexer Position", getPosition().get(Distance.Unit.INCH));
            SmartDashboard.putNumber("Indexer Set Position", distanceSetPoint.get(Distance.Unit.INCH));
            
 
            //setMotorSpeedInPercent(SmartDashboard.getNumber("Indexer Goal Speed", 0));
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
        return new boolean [] {EnabledSensors.getInstance().indexerPukeSensor.get(), EnabledSensors.getInstance().indexerSetting1.get(), EnabledSensors.getInstance().indexerSetting2.get(), EnabledSensors.getInstance().indexerSetting3.get(), EnabledSensors.getInstance().indexerShooterSensor.get()}; 
    }
 
    public void setPosition(Distance position){
        distanceSetPoint = position;
        if(indexerTalon != null){
            indexerTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
            System.out.println("setpos indexer" + distanceSetPoint.get(Distance.Unit.INCH));
            indexerTalon.set(ControlMode.Position, distanceSetPoint.get(Distance.Unit.MAGNETIC_ENCODER_TICK_INDEXER));
        }
    }

    public void deltaPosition(Distance position)
    {
        distanceSetPoint = getPosition().add(position);
        if(indexerTalon != null)
        {
            indexerTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
            indexerTalon.set(ControlMode.Position, distanceSetPoint.get(Unit.MAGNETIC_ENCODER_TICK_INDEXER));
        }
    }
 
    public void setSpeed(Speed target){
        speedSetPoint = target;
        
        if(indexerTalon != null){
            indexerTalon.selectProfileSlot(VEL_SLOT, DEFAULT_TIMEOUT);
            //System.out.println(target.get(Distance.Unit.INCH, Time.Unit.SECOND) + "setSpeeed working");
          //  System.out.println(target.get(Distance.Unit.MAGNETIC_ENCODER_TICK_INDEXER,Time.Unit.SECOND)+ "target unitys"); // always 400?
            indexerTalon.set(ControlMode.Velocity, target.get(Distance.Unit.MAGNETIC_ENCODER_TICK_INDEXER, Time.Unit.HUNDRED_MILLISECOND));
        }
    }
 
    public double getCurrent(){
        if(indexerTalon != null){
            return indexerTalon.getStatorCurrent();
        }
        return 0;
    }

    public boolean continueMoving(int numSensor){
        if(numSensor == 0)
        {
            return false;
        }

        else if(numSensor == 1)
        {
            return !EnabledSensors.getInstance().indexerSetting1.get();
        }

        else if(numSensor == 2)
        {
            return !EnabledSensors.getInstance().indexerSetting2.get();
        }

        else if(numSensor == 3)
        {
            return !EnabledSensors.getInstance().indexerSetting3.get();
        }

        else if(numSensor == 4)
        {
            return !EnabledSensors.getInstance().indexerShooterSensor.get();
        }
            return false;
    }

    public boolean readyToShoot(){
        return(EnabledSensors.getInstance().indexerShooterSensor.get());
    }
}