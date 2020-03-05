package edu.nr.robotics.subsystems.hood;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.*;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.motorcontrollers.SparkMax;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularAcceleration;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Angle.Unit;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
//import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Hood extends NRSubsystem {

    private static Hood singleton;

    private static CANSparkMax hoodSpark;

    private static CANEncoder hoodEncoder;

    public static final double ENCODER_TICKS_PER_DEGREE_HOOD = (4096 /360);

    public static final int VOLTAGE_COMPENSATION_LEVEL = 12;
    public static final double MIN_MOVE_VOLTAGE = 0.0; // unknown for sparkmax
    public static final int DEFAULT_TIMEOUT = 0;
    public static Time VOLTAGE_RAMP_RATE_HOOD = new Time(0.05, Time.Unit.SECOND);
    public static final double PROFILE_VEL_PERCENT_HOOD = 0.6; // change
    public static final double PROFILE_ACCEL_PERCENT_HOOD = 0.6;

    public static final double MOTION_MAGIC_PERCENT = 0.8; // n/a for sparkmax

    public static final AngularSpeed MAX_SPEED_HOOD = new AngularSpeed(45, Angle.Unit.DEGREE, Time.Unit.SECOND);
    public static final AngularAcceleration MAX_ACCELERATION_HOOD = new AngularAcceleration(200, Angle.Unit.DEGREE, Time.Unit.SECOND, Time.Unit.SECOND);

    // Change this

    public static final Angle upperMost = new Angle(45, Angle.Unit.DEGREE);
    public static final Angle lowerMost = Angle.ZERO;

    public static double F_POS_HOOD = 0.01;
    public static double P_POS_HOOD = 0.01;
    public static double I_POS_HOOD = 0;
    public static double D_POS_HOOD = 0;

    public static final int PEAK_CURRENT_HOOD = 60;
    public static final int CONTINUOUS_CURRENT_LIMIT_HOOD = 40;

    public static final NeutralMode NEUTRAL_MODE_HOOD = NeutralMode.Brake;

    public static final IdleMode IDLE_MODE_HOOD = IdleMode.kBrake; // for spark

    private AngularSpeed speedSetPointHood = AngularSpeed.ZERO;

    public static final Angle startAngle = new Angle(24, Angle.Unit.DEGREE);
    public static Angle setAngleHood = Angle.ZERO;
    public static Angle deltaAngleHood = Angle.ZERO;
    public static Angle goalAngleHood = Angle.ZERO;

    public static final Angle POINT_BLANK_SHOT_ANGLE = new Angle(26.25, Angle.Unit.DEGREE);

    public static final double motorRotationsPerHoodDegree = 1.815; // Motor Rev / Deg Hood

    public static double goalPercent = 0.0;

    public static final AngularAcceleration MAX_ACCEL = new AngularAcceleration(580, Angle.Unit.DEGREE,
            Time.Unit.SECOND, Time.Unit.SECOND);
    public static final AngularSpeed MAX_SPEED = new AngularSpeed(80, Angle.Unit.DEGREE, Time.Unit.SECOND);

    public static final int PID_TYPE = 0;

    public static final int VEL_SLOT = 0;
    public static final int POS_SLOT = 1;

    private double[] hoodAngles = {37.25, 39.5, 40, 41, 41.75, 43.1, 43.2, 42.75, 42.5, 42.4, 42.4, 42.35};
    private double[] limeLightDistances = {5.32, 6.38, 6.97, 7.4, 9.43, 10.55, 12.19, 12.6, 13.3, 14.1, 15.3, 17.6};

    private Hood() {
        if (EnabledSubsystems.HOOD_ENABLED) {

            hoodSpark = new CANSparkMax(RobotMap.HOOD_SPARK, MotorType.kBrushless);

            hoodEncoder = new CANEncoder(hoodSpark);

            hoodEncoder = hoodSpark.getEncoder(EncoderType.kHallSensor, 4096);

            hoodSpark.getPIDController().setFF(F_POS_HOOD, POS_SLOT);
            hoodSpark.getPIDController().setP(P_POS_HOOD, POS_SLOT);
            hoodSpark.getPIDController().setI(I_POS_HOOD, POS_SLOT);
            hoodSpark.getPIDController().setD(D_POS_HOOD, POS_SLOT);

            hoodSpark.setIdleMode(IDLE_MODE_HOOD);

            hoodSpark.setInverted(true);

            hoodSpark.setSmartCurrentLimit(CONTINUOUS_CURRENT_LIMIT_HOOD);
            hoodSpark.setSecondaryCurrentLimit(PEAK_CURRENT_HOOD);

            hoodSpark.enableVoltageCompensation(VOLTAGE_COMPENSATION_LEVEL);

            hoodSpark.setClosedLoopRampRate(VOLTAGE_RAMP_RATE_HOOD.get(Time.Unit.SECOND));
            hoodSpark.setOpenLoopRampRate(VOLTAGE_RAMP_RATE_HOOD.get(Time.Unit.SECOND));

            hoodSpark.getPIDController().setOutputRange(-1, 1, VEL_SLOT);
            hoodSpark.getPIDController().setOutputRange(-1, 1, POS_SLOT);

            hoodEncoder.setPosition(0);
        }
        smartDashboardInit();
    }

    public synchronized static void init() {
        if (singleton == null)
            singleton = new Hood();
        // if(EnabledSubsystems.HOOD_ENABLED)
        // singleton.setDefaultCommand(new HoodJoystickCommand());
    }

    public static Hood getInstance() {
        if (singleton == null) {
            init();
        }
        return singleton;
    }

    public static Angle getAngle() {
        if (hoodSpark != null) {
            return new Angle(hoodEncoder.getPosition(), Angle.Unit.ROTATION).add(startAngle);
        }
        return Angle.ZERO;
    }

    public void setAngle(Angle target) {
        if (hoodSpark != null) {
            //hoodSpark.set(target.get(Angle.Unit.DEGREE));

            //System.out.println("CALLED" + target.get(Angle.Unit.ROTATION));
            //hoodSpark.getPIDController().setReference(target.get(Angle.Unit.ROTATION) * 1.815, ControlType.kPosition, POS_SLOT);
            // System.out.println(setAngleHood.get(Angle.Unit.DEGREE) *
            // Hood.HoodDegreePerMotorRotation);
            if (target.get(Angle.Unit.DEGREE) < 24) {
                target = new Angle(24, Angle.Unit.DEGREE);
            }
            setAngleHood = target;
            Angle a = new Angle(target.get(Angle.Unit.DEGREE) - 24, Angle.Unit.DEGREE);
            double b = a.get(Angle.Unit.DEGREE) * motorRotationsPerHoodDegree;
            hoodSpark.getPIDController().setReference(b, ControlType.kPosition, POS_SLOT);
            // hoodSpark.getPIDController().seReference(setAngleHood.get(Angle.Unit.ROTATION),
            // ControlType.kPosition, POS_SLOT);
        }
    }

    public Angle getAngleLimelight()
    {
        double limeLightDistance = LimelightNetworkTable.getInstance().getDistance().get(Distance.Unit.FOOT);
        if(limeLightDistance >= limeLightDistances[0] && limeLightDistance <= limeLightDistances[limeLightDistances.length - 1])
        {
            int i;
            for(i = 0; i < limeLightDistances.length; i++)
            {
                if(limeLightDistances[i] > limeLightDistance)
                {
                    break;
                }
            }
            
            double slope = (hoodAngles[i] - hoodAngles[i - 1]) / (limeLightDistances[i] - limeLightDistances[i - 1]);
            double a = slope * (limeLightDistance - limeLightDistances[i - 1]);
            return new Angle(a, Angle.Unit.DEGREE);
        }

        else if(LimelightNetworkTable.getInstance().getDistance().get(Distance.Unit.FOOT) > 18.8)
        {

        }
        return Angle.ZERO;
    }

    public void smartDashboardInit() {
        if (EnabledSubsystems.HOOD_SMARTDASHBOARD_BASIC_ENABLED) {
            SmartDashboard.putNumber("Spark Encoder Position Hood", hoodEncoder.getPosition());

            SmartDashboard.putNumber("Spark Hood Current", hoodSpark.getOutputCurrent());
            SmartDashboard.putNumber("Hood Spark Angle", getAngle().get(Angle.Unit.DEGREE));

            // SmartDashboard.putNumber("Hood Percent", 0);

            SmartDashboard.putNumber("F_POS_HOOD", F_POS_HOOD);
            SmartDashboard.putNumber("P_POS_HOOD", P_POS_HOOD);
            SmartDashboard.putNumber("I_POS_HOOD", I_POS_HOOD);
            SmartDashboard.putNumber("D_POS_HOOD", D_POS_HOOD);

            SmartDashboard.putNumber("Hood Goal Angle", goalAngleHood.get(Angle.Unit.DEGREE));
            SmartDashboard.putNumber("Hood Delta Angle", deltaAngleHood.get(Angle.Unit.DEGREE));
            
            SmartDashboard.putBoolean("Lim Hood Lower", EnabledSensors.getInstance().LimHoodLower.get());
            SmartDashboard.putBoolean("Lim Hood Upper", EnabledSensors.getInstance().LimHoodUpper.get());
            SmartDashboard.putNumber("Limelight Calculated Distance", LimelightNetworkTable.getInstance().getDistance().get(Distance.Unit.FOOT));
            }
    }

    public void disable() {
        if (hoodSpark != null) {
            setAngle(Angle.ZERO);
        }
    }

    public AngularSpeed getSpeed() {

        if (hoodSpark != null) {
            return new AngularSpeed(hoodEncoder.getVelocity(), Angle.Unit.ROTATION, Time.Unit.MINUTE);
        }
        return AngularSpeed.ZERO;
    }

    public void setMotorSpeedRaw(double percent) {

        if (hoodSpark != null) {
            System.out.println(percent);
            hoodSpark.set(percent);
            //hoodSpark.getPIDController().setReference(percent, ControlType.kVoltage);
        }
    }

    public void smartDashboardInfo() {
        if (hoodSpark != null) {

            if (EnabledSubsystems.HOOD_SMARTDASHBOARD_BASIC_ENABLED) {

                SmartDashboard.putNumber("Spark Encoder Position Hood", hoodSpark.getEncoder().getPosition());

                SmartDashboard.putNumber("Spark Hood Current", hoodSpark.getOutputCurrent());

                SmartDashboard.putNumber("Hood Spark Angular Speed", hoodEncoder.getVelocity());
                SmartDashboard.putNumber("Hood Spark Angle", getAngle().get(Angle.Unit.DEGREE)); 
                
                SmartDashboard.putNumber("Hood setAngle", setAngleHood.get(Angle.Unit.DEGREE));

                F_POS_HOOD = SmartDashboard.getNumber("F_POS_HOOD", POS_SLOT);
                P_POS_HOOD = SmartDashboard.getNumber("P_POS_HOOD", POS_SLOT);
                I_POS_HOOD = SmartDashboard.getNumber("I_POS_HOOD", POS_SLOT);
                D_POS_HOOD = SmartDashboard.getNumber("D_POS_HOOD", POS_SLOT);

                hoodSpark.getPIDController().setFF(F_POS_HOOD, POS_SLOT);
                hoodSpark.getPIDController().setP(P_POS_HOOD, POS_SLOT);
                hoodSpark.getPIDController().setI(I_POS_HOOD, POS_SLOT);
                hoodSpark.getPIDController().setD(D_POS_HOOD, POS_SLOT);

                SmartDashboard.putBoolean("Lim Hood Lower", EnabledSensors.getInstance().LimHoodLower.get());

                SmartDashboard.putBoolean("Lim Hood Upper", EnabledSensors.getInstance().LimHoodUpper.get());

                // goalPercent = SmartDashboard.getNumber("Hood Percent", 0);

                goalAngleHood = new Angle(SmartDashboard.getNumber("Hood Goal Angle", goalAngleHood.get(Angle.Unit.DEGREE)), Angle.Unit.DEGREE);
                deltaAngleHood = new Angle(SmartDashboard.getNumber("Hood Delta Angle", deltaAngleHood.get(Angle.Unit.DEGREE)), Angle.Unit.DEGREE);
                SmartDashboard.putNumber("Limelight Calculated Distance", LimelightNetworkTable.getInstance().getDistance().get(Distance.Unit.FOOT));
                }
        }
        // System.out.println("Hood angle" + goalAngleHood.get(Angle.Unit.DEGREE));
    }

    public double getCurrent() {

        if (hoodSpark != null) {
            return hoodSpark.getOutputCurrent();
        }
        return 0;
    }

    public void periodic() {
        // check if limits are triggered, set to max / min hood spot if applicable
        if (EnabledSubsystems.HOOD_ENABLED) {
            if (EnabledSensors.getInstance().LimHoodLower.get()) {
                hoodSpark.getEncoder().setPosition(0);

                if (getSpeed().get(Angle.Unit.ROTATION, Time.Unit.MINUTE) < 0) { // might be backwards...
                    setMotorSpeedRaw(0);
                }
            }

            if (EnabledSensors.getInstance().LimHoodUpper.get()) {
                if(getSpeed().get(Angle.Unit.ROTATION, Time.Unit.MINUTE) > 0)
                {
                    setMotorSpeedRaw(0);
                }
            }
        }
    }
}