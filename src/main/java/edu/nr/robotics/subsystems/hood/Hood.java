package edu.nr.robotics.subsystems.hood;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.*;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.motorcontrollers.CTRECreator;
import edu.nr.lib.motorcontrollers.SparkMax;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularAcceleration;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.DigitalSensor;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
//import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Hood extends NRSubsystem {

    private static Hood singleton;

    private static CANSparkMax hoodSpark;

    public static final double ENCODER_TICKS_PER_DEGREE_HOOD = 2048 / 360; // unknown for sparkmax

    public static final int VOLTAGE_COMPENSATION_LEVEL = 12;
    public static final double MIN_MOVE_VOLTAGE = 0.0; // unknown for sparkmax
    public static final int DEFAULT_TIMEOUT = 0;
    public static Time VOLTAGE_RAMP_RATE_HOOD = new Time(0.05, Time.Unit.SECOND);
    public static final double PROFILE_VEL_PERCENT_HOOD = 0.6; // change
    public static final double PROFILE_ACCEL_PERCENT_HOOD = 0.6;

    public static final double MOTION_MAGIC_PERCENT = 0.8; // n/a for sparkmax

    public static final AngularSpeed MAX_SPEED_HOOD = new AngularSpeed(45, Angle.Unit.DEGREE, Time.Unit.SECOND);
    public static final AngularAcceleration MAX_ACCELERATION_HOOD = new AngularAcceleration(200, Angle.Unit.DEGREE,
            Time.Unit.SECOND, Time.Unit.SECOND);
    public DigitalSensor limSwitchTop = new DigitalSensor(RobotMap.LIM_HOOD_UPPER); // change IDs
    public DigitalSensor limSwitchBottom = new DigitalSensor(RobotMap.LIM_HOOD_LOWER);

    // Change this

    public static final Angle upperMost = new Angle(45, Angle.Unit.DEGREE);
    public static final Angle lowerMost = new Angle(0, Angle.Unit.DEGREE);

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

    public static final IdleMode IDLE_MODE_HOOD = IdleMode.kBrake; // for spark

    private AngularSpeed speedSetPointHood = AngularSpeed.ZERO;

    public static Angle setAngleHood = Angle.ZERO;
    public static Angle deltaAngleHood = Angle.ZERO;
    public static Angle goalAngleHood = Angle.ZERO;

    public static final int MOTION_MAGIC_SLOT = 2; // n/a for sparkmax
    public static final int MOTION_MAGIC_MULTIPLIER = 3; // Garrison Calculation Constant # 4

    public static final AngularAcceleration MAX_ACCEL = new AngularAcceleration(580, Angle.Unit.DEGREE,
            Time.Unit.SECOND, Time.Unit.SECOND);
    public static final AngularSpeed MAX_SPEED = new AngularSpeed(80, Angle.Unit.DEGREE, Time.Unit.SECOND);

    public static final int PID_TYPE = 0;

    public static final int VEL_SLOT = 0;
    public static final int POS_SLOT = 1;

    private Hood() {
        if (EnabledSubsystems.HOOD_ENABLED) {

            hoodSpark = SparkMax.createSpark(RobotMap.HOOD_TALON, MotorType.kBrushless);

            hoodSpark.getPIDController().setFF(F_POS_HOOD, 0);
            hoodSpark.getPIDController().setP(P_POS_HOOD, 0);
            hoodSpark.getPIDController().setI(I_POS_HOOD, 0);
            hoodSpark.getPIDController().setD(D_POS_HOOD, 0);

            hoodSpark.setIdleMode(IDLE_MODE_HOOD);

            hoodSpark.setInverted(true);

            hoodSpark.setSmartCurrentLimit(CONTINUOUS_CURRENT_LIMIT_HOOD);
            hoodSpark.setSecondaryCurrentLimit(PEAK_CURRENT_HOOD);

            hoodSpark.enableVoltageCompensation(VOLTAGE_COMPENSATION_LEVEL);

            hoodSpark.setClosedLoopRampRate(VOLTAGE_RAMP_RATE_HOOD.get(Time.Unit.SECOND));
            hoodSpark.setOpenLoopRampRate(VOLTAGE_RAMP_RATE_HOOD.get(Time.Unit.SECOND));

            hoodSpark.getPIDController().setOutputRange(-1, 1, VEL_SLOT);
            hoodSpark.getPIDController().setOutputRange(-1, 1, POS_SLOT);

        }
        smartDashboardInit();
    }

    public synchronized static void init() {
        if (singleton == null)
            singleton = new Hood();
    }

    public static Hood getInstance() {
        if (singleton == null) {
            init();
        }
        return singleton;
    }

    public static Angle getAngle() {
        if (hoodSpark != null) {
            return new Angle(hoodSpark.getEncoder().getPosition(), Angle.Unit.ROTATION);
        }
        return Angle.ZERO;
    }

    public void setAngle(Angle target) {
        setAngleHood = target;

        if (hoodSpark != null) {
            hoodSpark.getPIDController().setReference(setAngleHood.get(Angle.Unit.ROTATION), ControlType.kPosition,
                    POS_SLOT);
        }

    }

    public void smartDashboardInit() {
        if (EnabledSubsystems.HOOD_SMARTDASHBOARD_BASIC_ENABLED) {
            SmartDashboard.putNumber("Spark Encoder Position Hood", hoodSpark.getEncoder().getPosition());
            SmartDashboard.putNumber("Spark Hood Current", hoodSpark.getOutputCurrent());

            SmartDashboard.putNumber("F_POS_HOOD", F_POS_HOOD);
            SmartDashboard.putNumber("P_POS_HOOD", P_POS_HOOD);
            SmartDashboard.putNumber("I_POS_HOOD", I_POS_HOOD);
            SmartDashboard.putNumber("D_POS_HOOD", D_POS_HOOD);

            SmartDashboard.putNumber("F_VEL_HOOD", F_VEL_HOOD);
            SmartDashboard.putNumber("P_VEL_HOOD", P_VEL_HOOD);
            SmartDashboard.putNumber("I_VEL_HOOD", I_VEL_HOOD);
            SmartDashboard.putNumber("D_VEL_HOOD", D_VEL_HOOD);

            SmartDashboard.putNumber("Hood Goal Angle", goalAngleHood.get(Angle.Unit.DEGREE));
        }

    }

    public void disable() {
        if (hoodSpark != null) {
            setAngle(Angle.ZERO);
        }
    }

    public AngularSpeed getSpeed() {

        if (hoodSpark != null) {
            return new AngularSpeed(hoodSpark.getEncoder().getVelocity(), Angle.Unit.ROTATION, Time.Unit.MINUTE);
        }
        return AngularSpeed.ZERO;
    }

    public void setMotorSpeedRaw(double percent) {

        if (hoodSpark != null) {
            hoodSpark.set(percent);
        }
    }

    public void smartDashboardInfo() {
        if (hoodSpark != null) {

            if (EnabledSubsystems.HOOD_SMARTDASHBOARD_BASIC_ENABLED) {
                SmartDashboard.putNumber("Spark Encoder Position Hood", hoodSpark.getEncoder().getPosition());
                SmartDashboard.putNumber("Spark Hood Current", hoodSpark.getOutputCurrent());

                SmartDashboard.putNumber("Hood Spark Angular Speed", hoodSpark.getEncoder().getVelocity());
                SmartDashboard.putNumber("Hood Spark Angle", getAngle().get(Angle.Unit.DEGREE));
                SmartDashboard.putNumber("Hood setAngle", setAngleHood.get(Angle.Unit.DEGREE));

                F_POS_HOOD = SmartDashboard.getNumber("F_POS_HOOD", 0);
                P_POS_HOOD = SmartDashboard.getNumber("P_POS_HOOD", 0);
                I_POS_HOOD = SmartDashboard.getNumber("I_POS_HOOD", 0);
                D_POS_HOOD = SmartDashboard.getNumber("D_POS_HOOD", 0);

                F_VEL_HOOD = SmartDashboard.getNumber("F_VEL_HOOD", 0);
                P_VEL_HOOD = SmartDashboard.getNumber("P_VEL_HOOD", 0);
                I_VEL_HOOD = SmartDashboard.getNumber("I_VEL_HOOD", 0);
                D_VEL_HOOD = SmartDashboard.getNumber("D_VEL_HOOD", 0);

            }
            goalAngleHood = new Angle(SmartDashboard.getNumber("Hood Goal Angle", goalAngleHood.get(Angle.Unit.DEGREE)),
                    Angle.Unit.DEGREE);

        }
        // System.out.println("Hood angle" + goalAngleHood.get(Angle.Unit.DEGREE));
    }

    public double getCurrent() {

        if (hoodSpark != null) {
            return hoodSpark.getOutputCurrent();
        }
        return 0;
    }

        public void periodic()
        {
            //check if limits are triggered, set to max / min hood spot if applicable
            
            /*if(EnabledSensors.getInstance().LimHoodLower.get()){
                hoodSpark.getEncoder().setPosition(0); // should reset position

                if (getSpeed().get(Angle.Unit.ROTATION, Time.Unit.MINUTE) < 0) { // might be backwards...
                    setMotorSpeedRaw(0);
                }
            }

            if (setAngleHood.get(Angle.Unit.DEGREE) > 70) {
                setAngle(new Angle(70, Angle.Unit.DEGREE));
            }*/
        //}
    }
}