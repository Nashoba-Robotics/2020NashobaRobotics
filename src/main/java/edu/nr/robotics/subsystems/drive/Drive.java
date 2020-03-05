package edu.nr.robotics.subsystems.drive;

import java.io.File;
import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.nr.lib.NRMath;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.driving.DriveTypeCalculations;
import edu.nr.lib.gyro.Pigeon;
import edu.nr.lib.gyro.ResetGyroCommand;
import edu.nr.lib.interfaces.DoublePIDOutput;
import edu.nr.lib.interfaces.DoublePIDSource;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfilerTwoMotor;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectoryRamped;
import edu.nr.lib.motionprofiling.TwoDimensionalMotionProfilerPathfinder;
import edu.nr.lib.motionprofiling.PIDSourceType;

import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.units.Acceleration;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Distance.Unit;
import edu.nr.lib.units.Jerk;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Waypoint;


public class Drive extends NRSubsystem implements DoublePIDOutput, DoublePIDSource {

//RIP TestCommandZebra

    private static Drive singleton;

    private TalonFX leftDrive, rightDrive;

    private TalonFX leftDriveFollow1, leftDriveFollow2, rightDriveFollow1, rightDriveFollow2;

    private PowerDistributionPanel pdp;
    // these may change because of new talons

    // TODO: fix all of these
    /*
    public static final double REAL_ENC_TICK_PER_INCH_DRIVE = 898;

    public static final double EFFECTIVE_ENC_TICK_PER_INCH_DRIVE = REAL_ENC_TICK_PER_INCH_DRIVE * 1.01; // * 1.02;

    public static final Distance WHEEL_DIAMETER = new Distance(6.25, Distance.Unit.INCH);
    public static final Distance WHEEL_DIAMETER_EFFECTIVE = new Distance(6.25, Distance.Unit.INCH);

    public static double wheelBaseMultiplier = 1.3;
    public static final Distance WHEEL_BASE = new Distance(23.5, Distance.Unit.INCH);
    */

    public static final double REAL_ENC_TICK_PER_INCH_DRIVE = (2048 * (8 + 1/3.0)) / (6 * Math.PI);

	public static final double EFFECTIVE_ENC_TICK_PER_INCH_DRIVE = REAL_ENC_TICK_PER_INCH_DRIVE * 1.01; // * 1.02;

	public static final Distance WHEEL_DIAMETER = new Distance(6, Distance.Unit.INCH);
	public static final Distance WHEEL_DIAMETER_EFFECTIVE = new Distance(6, Distance.Unit.INCH);

	public static double wheelBaseMultiplier = 1.3;
	public static final Distance WHEEL_BASE = new Distance(24, Distance.Unit.INCH);

    public static final Speed MAX_SPEED_DRIVE = new Speed(18, Distance.Unit.FOOT, Time.Unit.SECOND);

    public static final Acceleration MAX_ACCEL_DRIVE = new Acceleration(10, Distance.Unit.FOOT, Time.Unit.SECOND, Time.Unit.SECOND);

    public static final Jerk MAX_JERK_DRIVE = new Jerk(100, Distance.Unit.FOOT, Time.Unit.SECOND, Time.Unit.SECOND,
            Time.Unit.SECOND);

    public static final double MIN_MOVE_VOLTAGE_PERCENT_LEFT = 0.077;//0.065
    public static final double MIN_MOVE_VOLTAGE_PERCENT_RIGHT = 0.077; // .095

    public static double goalPercent = 0.0;

    public static final double VOLTAGE_PERCENT_VELOCITY_SLOPE_LEFT = 0.037; // .065
    public static final double VOLTAGE_PERCENT_VELOCITY_SLOPE_RIGHT = 0.037; // .0645

	public static Time DRIVE_RAMP_RATE = new Time(1.6, Time.Unit.SECOND); //no lower than 1.6

    public static double P_LEFT = 0.0;
    public static double I_LEFT = 0.0;
    public static double D_LEFT = 0.0;

    public static double P_RIGHT = 0.0;
    public static double I_RIGHT = 0.0;
    public static double D_RIGHT = 0.0;

    public static double kVOneD = 1
            / MAX_SPEED_DRIVE.get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE, Time.Unit.HUNDRED_MILLISECOND);
    public static double kAOneD = 0.0;
    public static double kPOneD = 0.000000002;
    public static double kIOneD = 0;
    public static double kDOneD = 0.0000000002;
    public static double kP_thetaOneD = 0;

    public static double kVTwoD = 1
            / MAX_SPEED_DRIVE.get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE, Time.Unit.HUNDRED_MILLISECOND);
    public static double kATwoD = 0.0002;
    public static double kPTwoD = 0.000000002;
    public static double kITwoD = 0;
    public static double kDTwoD = 0.0000000002;
    public static double kP_thetaTwoD = 0.00005;

    public static final double ONE_D_PROFILE_DRIVE_PERCENT = 0.6;   
    public static final double TWO_D_PROFILE_DRIVE_PERCENT = 0.4;
    public static final double ONE_D_ACCEL_PERCENT = 0.8;
    public static final double TWO_D_ACCEL_PERCENT = 0.6;

    public static double TURN_JOYSTICK_MULTIPLIER = .7;
    public static double MOVE_JOYSTICK_MULTIPLIER = 1;

    public static final double MAX_PROFILE_TURN_PERCENT = 1;
    public static final double MIN_PROFILE_TURN_PERCENT = 0.02;
	
    public static final Distance END_THRESHOLD = new Distance(3, Distance.Unit.INCH);
    public static final Speed PROFILE_END_TURN_SPEED_THRESHOLD = MAX_SPEED_DRIVE.mul(MIN_PROFILE_TURN_PERCENT + 0.01);
    public static final Speed PROFILE_END_SPEED_THRESHOLD = MAX_SPEED_DRIVE.mul(0.10);
 
    public static final Angle DRIVE_ANGLE_THRESHOLD = new Angle(2, Angle.Unit.DEGREE); // change?
    public static final Angle DRIVE_STOP_ANGLE = new Angle(45, Angle.Unit.DEGREE); // find angle that robot stops at
                                                                                    // when turning goes from 1 to 0
    private static final int PEAK_DRIVE_CURRENT = 80;// amps
    private static final int PEAK_DRIVE_CURRENT_DURATION = 1000;// miliseconds, so one second
    private static final int CONTINUOUS_CURRENT_LIMIT = 40; // amps

    public static final VelocityMeasPeriod VELOCITY_MEASUREMENT_PERIOD_DRIVE = VelocityMeasPeriod.Period_10Ms; // find
    public static final int VELOCITY_MEASUREMENT_WINDOW_DRIVE = 32; // find this

    public static final int VOLTAGE_COMPENSATION_LEVEL = 12;

    public static final NeutralMode NEUTRAL_MODE = NeutralMode.Coast;

    // Type of PID. 0 = primary. 1 = cascade
    public static final int PID_TYPE = 0;

    public static final int POS_SLOT = 0;

    public static final int VEL_SLOT = 1;

    // No timeout for talon configuration functions
    public static final int DEFAULT_TIMEOUT = 0;

    public static boolean sniperModeEnabled = false;
    public static boolean pushModeEnabled = false;

    // tracking drive motor setpoints
    private Speed leftMotorSetpoint = Speed.ZERO;
    private Speed rightMotorSetpoint = Speed.ZERO;
    private double oldTurn;

    private PIDSourceType type = PIDSourceType.kRate;

    public static double timeMult = 70;
    public static Distance endX;
    public static Distance endY;
    public static Angle endAngle = new Angle(0, Angle.Unit.DEGREE); // set next two to SD...
    public static Distance xPoint1 = new Distance(1, Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE);
    public static Distance yPoint1 = Distance.ZERO;
    public static Angle anglePoint1 = Angle.ZERO;
    public static String profileName = "ProfileName";
    public static double oneDDrivePercent;
    public static double oneDAccelPercent;
    public static double twoDDrivePercent;
    public static double twoDAccelPercent;
    public static Angle angleToTurn;
 
    public static ArrayList<Double> leftDriveVelocities = new ArrayList<Double>();
    public static ArrayList<Double> rightDriveVelocities = new ArrayList<Double>();

    public OneDimensionalMotionProfilerTwoMotor diagonalProfiler;
    public TwoDimensionalMotionProfilerPathfinder twoDProfiler;
    private Waypoint[] points;

    double speed = 0;
 
    public static enum DriveMode {
        arcadeDrive, tankDrive, cheesyDrive, fieldCentricDrive
    }
 
    private Drive() {
        if (EnabledSubsystems.DRIVE_ENABLED) {
            leftDrive = new TalonFX(RobotMap.LEFT_DRIVE);
            rightDrive = new TalonFX(RobotMap.RIGHT_DRIVE);

            pdp = new PowerDistributionPanel(RobotMap.PDP_ID);
 
            leftDriveFollow1 = new TalonFX(RobotMap.LEFT_DRIVE_FOLLOW_1);
            leftDriveFollow1.follow(leftDrive);
            leftDriveFollow2 = new TalonFX(RobotMap.LEFT_DRIVE_FOLLOW_2);
            leftDriveFollow2.follow(leftDrive);
 
            rightDriveFollow1 = new TalonFX(RobotMap.RIGHT_DRIVE_FOLLOW_1);
            rightDriveFollow1.follow(rightDrive);
            rightDriveFollow2 = new TalonFX(RobotMap.RIGHT_DRIVE_FOLLOW_2);
            rightDriveFollow2.follow(rightDrive);
 
            if (EnabledSubsystems.DRIVE_DUMB_ENABLED) {
                leftDrive.set(ControlMode.PercentOutput, 0);
                rightDrive.set(ControlMode.PercentOutput, 0);
            } else {
                leftDrive.set(ControlMode.Velocity, 0);
                rightDrive.set(ControlMode.Velocity, 0);
            }
 
            leftDrive.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, PID_TYPE, DEFAULT_TIMEOUT);
            rightDrive.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, PID_TYPE, DEFAULT_TIMEOUT);
            
            leftDrive.config_kF(VEL_SLOT, 0, DEFAULT_TIMEOUT);
            leftDrive.config_kP(VEL_SLOT, P_LEFT, DEFAULT_TIMEOUT);
            leftDrive.config_kI(VEL_SLOT, I_LEFT, DEFAULT_TIMEOUT);
            leftDrive.config_kD(VEL_SLOT, D_LEFT, DEFAULT_TIMEOUT);
 
            leftDrive.setNeutralMode(NEUTRAL_MODE);
            leftDriveFollow1.setNeutralMode(NEUTRAL_MODE);
            leftDriveFollow2.setNeutralMode(NEUTRAL_MODE);
 
            leftDrive.setInverted(false);
            leftDriveFollow1.setInverted(false);
            leftDriveFollow2.setInverted(false);
 
            leftDrive.setSensorPhase(true);
 
            leftDrive.enableVoltageCompensation(true);
            leftDrive.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);
 
        //    leftDrive.enableCurrentLimit(true);
        //    leftDrive.configPeakCurrentLimit(PEAK_DRIVE_CURRENT, DEFAULT_TIMEOUT);
        //    leftDrive.configPeakCurrentDuration(PEAK_DRIVE_CURRENT_DURATION, DEFAULT_TIMEOUT);
        //    leftDrive.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT, DEFAULT_TIMEOUT);
 
            leftDrive.configVelocityMeasurementPeriod(VELOCITY_MEASUREMENT_PERIOD_DRIVE, DEFAULT_TIMEOUT);
            leftDrive.configVelocityMeasurementWindow(VELOCITY_MEASUREMENT_WINDOW_DRIVE, DEFAULT_TIMEOUT);
 
            leftDrive.configClosedloopRamp(DRIVE_RAMP_RATE.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
            leftDrive.configOpenloopRamp(DRIVE_RAMP_RATE.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
 
            leftDrive.selectProfileSlot(VEL_SLOT, DEFAULT_TIMEOUT);
 
            rightDrive.config_kF(VEL_SLOT, 0, DEFAULT_TIMEOUT);
            rightDrive.config_kP(VEL_SLOT, P_RIGHT, DEFAULT_TIMEOUT);
            rightDrive.config_kI(VEL_SLOT, I_RIGHT, DEFAULT_TIMEOUT);
            rightDrive.config_kD(VEL_SLOT, D_RIGHT, DEFAULT_TIMEOUT);
 
            rightDrive.setNeutralMode(NEUTRAL_MODE);
            rightDriveFollow1.setNeutralMode(NEUTRAL_MODE);
            rightDriveFollow2.setNeutralMode(NEUTRAL_MODE);
 
            rightDrive.setInverted(true);
            rightDriveFollow1.setInverted(true);
            rightDriveFollow2.setInverted(true);
 
            rightDrive.setSensorPhase(false);
 
            rightDrive.enableVoltageCompensation(true);
            rightDrive.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);
 
        //    rightDrive.enableCurrentLimit(true);
        //    rightDrive.configPeakCurrentLimit(PEAK_DRIVE_CURRENT, DEFAULT_TIMEOUT);
        //    rightDrive.configPeakCurrentDuration(PEAK_DRIVE_CURRENT_DURATION, DEFAULT_TIMEOUT);
        //    rightDrive.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT, DEFAULT_TIMEOUT);
 
            rightDrive.configVelocityMeasurementPeriod(VELOCITY_MEASUREMENT_PERIOD_DRIVE, DEFAULT_TIMEOUT);
            rightDrive.configVelocityMeasurementWindow(VELOCITY_MEASUREMENT_WINDOW_DRIVE, DEFAULT_TIMEOUT);
 
            rightDrive.configClosedloopRamp(DRIVE_RAMP_RATE.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
            rightDrive.configOpenloopRamp(DRIVE_RAMP_RATE.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
 
            rightDrive.selectProfileSlot(VEL_SLOT, DEFAULT_TIMEOUT);
 
            smartDashboardInit();
 
            CheesyDriveCalculationConstants.createDriveTypeCalculations();
 
            LimelightNetworkTable.getInstance();
        }
    }
 
    public synchronized static void init() {
        if (singleton == null) {
            singleton = new Drive();
            if (EnabledSubsystems.DRIVE_ENABLED)
                singleton.setDefaultCommand(new DriveJoystickCommand());
        }
    }
 
    public static Drive getInstance() {
        if (singleton == null)
            init();
        return singleton;
    }
 
    public Distance getRightPosition() {
        if (rightDrive != null) {
            //return new Distance(rightDrive.getSelectedSensorPosition(PID_TYPE), Unit.MAGNETIC_ENCODER_TICK_DRIVE);
            return new Distance(rightDrive.getSensorCollection().getIntegratedSensorPosition(), Unit.MAGNETIC_ENCODER_TICK_DRIVE);
        } else {
            return Distance.ZERO;
        }
    }
 
    public Distance getLeftPosition() {
        if (leftDrive != null) {
            //return new Distance(leftDrive.getSelectedSensorPosition(PID_TYPE), Unit.MAGNETIC_ENCODER_TICK_DRIVE);
            return new Distance(leftDrive.getSensorCollection().getIntegratedSensorPosition(), Unit.MAGNETIC_ENCODER_TICK_DRIVE);
        } else {
            return Distance.ZERO;
        }
    }
 
    public Speed getLeftVelocity() {
        if (leftDrive != null)
            //return new Speed(leftDrive.getSelectedSensorVelocity(VEL_SLOT), Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE, Time.Unit.HUNDRED_MILLISECOND);
            return new Speed(leftDrive.getSensorCollection().getIntegratedSensorVelocity(), Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE, Time.Unit.HUNDRED_MILLISECOND);
        return Speed.ZERO;
    }
 
    public Speed getRightVelocity() {
        if (rightDrive != null)
            //return new Speed(rightDrive.getSelectedSensorVelocity(VEL_SLOT), Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE, Time.Unit.HUNDRED_MILLISECOND);
            return new Speed(rightDrive.getSensorCollection().getIntegratedSensorVelocity(), Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE, Time.Unit.HUNDRED_MILLISECOND);
        return Speed.ZERO;
    }
 
    public double getRightCurrent() {
        if (rightDrive != null) {
            return rightDrive.getStatorCurrent();
        }
        return 0;
    }
 
    public double getRightFollow1Current() {
        if (rightDriveFollow1 != null){

        }
            //return rightDriveFollow1.getStatorCurrent();
        return 0;
    }
 
    public double getRightFollow2Current() {
        if (rightDriveFollow2 != null){

        }
            //return rightDriveFollow2.getStatorCurrent();
        return 0;
    }
 
    public double getLeftCurrent() {
        if (leftDrive != null) {
            return leftDrive.getStatorCurrent();
        }
        return 0;
    }
 
    public double getLeftFollow1Current() {
        if (leftDriveFollow1 != null){
            return leftDriveFollow1.getStatorCurrent();
        }
        return 0;
    }
 
    public double getLeftFollow2Current() {
        if (leftDriveFollow2 != null){
            return leftDriveFollow2.getStatorCurrent();
        }
        return 0;
    }
 
    public void stayInPlaceOnStart()
    {
        leftDrive.getSensorCollection().setIntegratedSensorPosition(0, DEFAULT_TIMEOUT);
        rightDrive.getSensorCollection().setIntegratedSensorPosition(0, DEFAULT_TIMEOUT);
    }

    public void stayInPlaceOnExecute()
    {
        leftDrive.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
        leftDrive.set(ControlMode.Position, 0);

        rightDrive.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
        rightDrive.set(ControlMode.Position, 0);
    }

    public void stayInPlaceOnEnd()
    {        
        leftDrive.set(ControlMode.PercentOutput, 0);
        rightDrive.set(ControlMode.PercentOutput, 0);
    }
 
    public void setMotorSpeedInPercent(double left, double right) {
        if (EnabledSubsystems.DRIVE_DUMB_ENABLED) {
            leftDrive.set(ControlMode.PercentOutput, left);
            rightDrive.set(ControlMode.PercentOutput, right);
        }
        else {
            setMotorSpeed(MAX_SPEED_DRIVE.mul(left), MAX_SPEED_DRIVE.mul(right));
        }
    }
 
    public void setMotorSpeed(Speed left, Speed right) {
        if (leftDrive != null && rightDrive != null) {
            leftMotorSetpoint = left;
            rightMotorSetpoint = right;
 
            leftDrive.config_kF(VEL_SLOT,
                    ((VOLTAGE_PERCENT_VELOCITY_SLOPE_LEFT
                            * leftMotorSetpoint.abs().get(Distance.Unit.FOOT, Time.Unit.SECOND)
                            + MIN_MOVE_VOLTAGE_PERCENT_LEFT) * 1023.0)
                            / leftMotorSetpoint.abs().get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE,
                                    Time.Unit.HUNDRED_MILLISECOND),
                    DEFAULT_TIMEOUT);
 
            rightDrive.config_kF(VEL_SLOT,
                    ((VOLTAGE_PERCENT_VELOCITY_SLOPE_RIGHT
                            * rightMotorSetpoint.abs().get(Distance.Unit.FOOT, Time.Unit.SECOND)
                            + MIN_MOVE_VOLTAGE_PERCENT_RIGHT) * 1023.0)
                            / rightMotorSetpoint.abs().get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE,
                                    Time.Unit.HUNDRED_MILLISECOND),
                    DEFAULT_TIMEOUT);
 
            if (EnabledSubsystems.DRIVE_DUMB_ENABLED) {
                leftDrive.set(leftDrive.getControlMode(), leftMotorSetpoint.div(MAX_SPEED_DRIVE));
                rightDrive.set(rightDrive.getControlMode(), rightMotorSetpoint.div(MAX_SPEED_DRIVE));
            } else {
                leftDrive.set(leftDrive.getControlMode(), leftMotorSetpoint
                        .get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE, Time.Unit.HUNDRED_MILLISECOND));
                rightDrive.set(rightDrive.getControlMode(), rightMotorSetpoint
                        .get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE, Time.Unit.HUNDRED_MILLISECOND));
            }
        }
    }
 
    public void setVoltageRamp(Time time) {
        leftDrive.configClosedloopRamp(time.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
        rightDrive.configClosedloopRamp(time.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
        leftDrive.configOpenloopRamp(time.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
        rightDrive.configOpenloopRamp(time.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
    }
 
    public void tankDrive(double left, double right) {
        setMotorSpeedInPercent(left, right);
    }
 
    public void arcadeDrive(double move, double turn) {
        double[] motorPercents = DriveTypeCalculations.arcadeDrive(move, turn);
 
        tankDrive(motorPercents[0], motorPercents[1]);
    }
 
    public void cheesyDrive(double move, double turn) {
        double[] cheesyMotorPercents = new double[2];
        cheesyMotorPercents = DriveTypeCalculations.cheesyDrive(move, turn, oldTurn, false);
 
        oldTurn = turn;
 
        tankDrive(cheesyMotorPercents[0], cheesyMotorPercents[1]);
    }
 
    public void setPIDSourceType(PIDSourceType pidSource) {
        type = pidSource;
    }
 
    public PIDSourceType getPIDSourceType() {
        return type;
    }
 
    public double pidGetRight() {
        if (type == PIDSourceType.kRate) {
            return getInstance().getRightVelocity().get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE,
                    Time.Unit.HUNDRED_MILLISECOND);
        } else {
            return getInstance().getRightPosition().get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE);
        }
    }
 
    public double pidGetLeft() {
        if (type == PIDSourceType.kRate) {
            return getInstance().getLeftVelocity().get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE,
                    Time.Unit.HUNDRED_MILLISECOND);
        } else {
            return getInstance().getLeftPosition().get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE);
        }
    }
 
    public void pidWrite(double outputLeft, double outputRight) {
        setMotorSpeedInPercent(outputLeft, outputRight);
    }
 
    public void enableTwoDMotionProfiler(Distance endX, Distance endY, Angle endAngle, Distance x1Point,
            Distance y1Point, Angle angle1Point, double maxVelPercent, double maxAccelPercent, String profileName) {
        File profileFile = new File("home/lvuser/profiles/" + profileName + ".traj");
 
        twoDProfiler = new TwoDimensionalMotionProfilerPathfinder(this, this, kVTwoD, kATwoD, kPTwoD, kITwoD, kDTwoD,
                kP_thetaTwoD,
                MAX_SPEED_DRIVE.mul(maxVelPercent).get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE,
                        Time.Unit.HUNDRED_MILLISECOND),
                MAX_ACCEL_DRIVE.mul(maxAccelPercent).get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE,
                        Time.Unit.HUNDRED_MILLISECOND, Time.Unit.HUNDRED_MILLISECOND),
                MAX_JERK_DRIVE.get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE, Time.Unit.HUNDRED_MILLISECOND,
                        Time.Unit.HUNDRED_MILLISECOND, Time.Unit.HUNDRED_MILLISECOND),
                (int) (Math.PI * WHEEL_DIAMETER_EFFECTIVE.get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE)),
                WHEEL_DIAMETER.get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE),
                WHEEL_BASE.mul(wheelBaseMultiplier).get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE), false, profileFile);
 
        //System.out.println(profileFile.getName());
 
        if (!profileFile.exists()) {
            System.out.println("endX: " + endX.get(Distance.Unit.FOOT) + "  endY: " + endY.get(Distance.Unit.FOOT)
                    + " end Angle: " + endAngle.get(Angle.Unit.DEGREE));
            points = new Waypoint[] { new Waypoint(0, 0, 0),
                    new Waypoint(x1Point.get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE),
                            y1Point.get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE), angle1Point.get(Angle.Unit.RADIAN)),
                    new Waypoint(endX.get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE),
                            endY.get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE), endAngle.get(Angle.Unit.RADIAN)) };
        }
        twoDProfiler.setTrajectory(points);
 
        twoDProfiler.enable();
 
    }
 
    public void enableMotionProfiler(Distance distX, double maxVelPercent, double maxAccelPercent) {
        double minVel;
        double minAccel = 0;
 
        if (!distX.equals(Distance.ZERO)) {
            minVel = MAX_SPEED_DRIVE.mul(maxVelPercent).get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE,
                    Time.Unit.HUNDRED_MILLISECOND);
            minAccel = MAX_ACCEL_DRIVE.mul(maxAccelPercent).get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE,
                    Time.Unit.HUNDRED_MILLISECOND, Time.Unit.HUNDRED_MILLISECOND);
        } else {
            minVel = 0;
            minAccel = 0;
            System.out.println("No Distances Set");
        }
        // fix
        /*System.out.println("distX: " + distX.get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE));
        System.out.println("minvel: " + minVel);
        System.out.println("minaccel: " + minAccel);*/
 
        diagonalProfiler = new OneDimensionalMotionProfilerTwoMotor(this, this, kVOneD, kAOneD, kPOneD, kIOneD, kDOneD,
                kP_thetaOneD);
        diagonalProfiler.setTrajectory(new OneDimensionalTrajectoryRamped(
                distX.get(Distance.Unit.MAGNETIC_ENCODER_TICK_DRIVE), minVel, minAccel, timeMult));
        diagonalProfiler.enable();
    }
 
    public void disableProfiler() {
        if (diagonalProfiler != null)
            diagonalProfiler.disable();
        if (twoDProfiler != null)
            twoDProfiler.disable();
    }
 
    private void smartDashboardInit() {
 
    //    SmartDashboard.putNumber("MOTOR RUNNING", 0);////test
 
        if (EnabledSubsystems.DRIVE_SMARTDASHBOARD_BASIC_ENABLED) {
            SmartDashboard.putData(new ResetGyroCommand());
        }
        if (EnabledSubsystems.DRIVE_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putNumber("Wheel Base Multiplier: ", wheelBaseMultiplier);
 
            SmartDashboard.putNumber("Left P Value: ", P_LEFT);
            SmartDashboard.putNumber("Left I Value: ", I_LEFT);
            SmartDashboard.putNumber("Left D Value: ", D_LEFT);
 
            SmartDashboard.putNumber("Right P Value: ", P_RIGHT);
            SmartDashboard.putNumber("Right I Value: ", I_RIGHT);
            SmartDashboard.putNumber("Right D Value: ", D_RIGHT);
 
            SmartDashboard.putNumber("kVOneD Value: ", kVOneD);
            SmartDashboard.putNumber("kAOneD Value: ", kAOneD);
            SmartDashboard.putNumber("kPOneD Value: ", kPOneD);
            SmartDashboard.putNumber("kIOneD Value: ", kIOneD);
            SmartDashboard.putNumber("kDOneD Value: ", kDOneD);
            SmartDashboard.putNumber("kP_thetaOneD Value: ", kP_thetaOneD);
 
            SmartDashboard.putNumber("kVTwoD Value: ", kVTwoD);
            SmartDashboard.putNumber("kATwoD Value: ", kATwoD);
            SmartDashboard.putNumber("kPTwoD Value: ", kPTwoD);
            SmartDashboard.putNumber("kITwoD Value: ", kITwoD);
            SmartDashboard.putNumber("kDTwoD Value: ", kDTwoD);
            SmartDashboard.putNumber("kP_thetaTwoD Value: ", kP_thetaTwoD);
 
            SmartDashboard.putNumber("Drive Ramp Rate: ", DRIVE_RAMP_RATE.get(Time.Unit.SECOND));
 
            SmartDashboard.putNumber("X End Point: ", 0);
            SmartDashboard.putNumber("Y End Point: ", 0);
            SmartDashboard.putNumber("Profile End Angle: ", endAngle.get(Angle.Unit.DEGREE));
            SmartDashboard.putNumber("Point 1 X: ", xPoint1.get(Distance.Unit.FOOT));
            SmartDashboard.putNumber("Point 1 Y: ", yPoint1.get(Distance.Unit.FOOT));
            SmartDashboard.putNumber("Point 1 Angle: ", anglePoint1.get(Angle.Unit.DEGREE));
            SmartDashboard.putString("Profile Name: ", profileName);
            SmartDashboard.putNumber("One D Drive Percent: ", ONE_D_PROFILE_DRIVE_PERCENT);
            SmartDashboard.putNumber("One D Drive Accel Percent: ", ONE_D_ACCEL_PERCENT);
            SmartDashboard.putNumber("Two D Drive Percent: ", TWO_D_PROFILE_DRIVE_PERCENT);
            SmartDashboard.putNumber("Two D Drive Accel Percent: ", TWO_D_ACCEL_PERCENT);
            SmartDashboard.putNumber("Angle To Turn: ", 0);

            SmartDashboard.putNumber("Goal Percent", 0);
        }
 
    }
 
    public void smartDashboardInfo() {
        if (leftDrive != null && rightDrive != null) {
        //    speed = SmartDashboard.getNumber("MOTOR RUNNING", speed);
        //    setMotorSpeedInPercent(speed, speed);
            
 
            if (EnabledSubsystems.DRIVE_SMARTDASHBOARD_BASIC_ENABLED) {
 
                SmartDashboard.putNumberArray("Drive Left Current",
                        new double[] { getLeftCurrent(), getLeftFollow1Current(), getLeftFollow2Current() });
                SmartDashboard.putNumberArray("Drive Right Current",
                        new double[] { getRightCurrent(), getRightFollow1Current(), getRightFollow2Current() });
 
                SmartDashboard.putNumber("Gyro Yaw",
                        (-Pigeon.getPigeon(RobotMap.PIGEON_ID).getYaw().get(Angle.Unit.DEGREE)) % 360);
 
                SmartDashboard.putNumberArray("Drive Left Velocity",
                        new double[] { getLeftVelocity().get(Distance.Unit.FOOT, Time.Unit.SECOND),
                                leftMotorSetpoint.get(Distance.Unit.FOOT, Time.Unit.SECOND) });
                SmartDashboard.putNumberArray("Drive Right Velocity",
                        new double[] { getRightVelocity().get(Distance.Unit.FOOT, Time.Unit.SECOND),
                                rightMotorSetpoint.get(Distance.Unit.FOOT, Time.Unit.SECOND) });
            }
            if (EnabledSubsystems.DRIVE_SMARTDASHBOARD_DEBUG_ENABLED) {
                SmartDashboard.putNumber("Drive Left Percent", leftMotorSetpoint.div(MAX_SPEED_DRIVE));
                SmartDashboard.putNumber("Drive Right Percent", rightMotorSetpoint.div(MAX_SPEED_DRIVE));
 
                SmartDashboard.putNumber("Drive Left Position", getLeftPosition().get(Distance.Unit.INCH));
                SmartDashboard.putNumber("Drive Right Position", getRightPosition().get(Distance.Unit.INCH));
 
                SmartDashboard.putNumber("Drive Left Encoder Position", leftDrive.getSensorCollection().getIntegratedSensorPosition());
                SmartDashboard.putNumber("Drive Right Encoder Position", rightDrive.getSensorCollection().getIntegratedSensorPosition());
 
                leftDrive.config_kP(VEL_SLOT, SmartDashboard.getNumber("Left P Value: ", P_LEFT), DEFAULT_TIMEOUT);
                leftDrive.config_kI(VEL_SLOT, SmartDashboard.getNumber("Left I Value: ", I_LEFT), DEFAULT_TIMEOUT);
                leftDrive.config_kD(VEL_SLOT, SmartDashboard.getNumber("Left D Value: ", D_LEFT), DEFAULT_TIMEOUT);
 
                rightDrive.config_kP(VEL_SLOT, SmartDashboard.getNumber("Right P Value: ", P_RIGHT), DEFAULT_TIMEOUT);
                rightDrive.config_kI(VEL_SLOT, SmartDashboard.getNumber("Right I Value: ", I_RIGHT), DEFAULT_TIMEOUT);
                rightDrive.config_kD(VEL_SLOT, SmartDashboard.getNumber("Right D Value: ", D_RIGHT), DEFAULT_TIMEOUT);
 
                kVOneD = SmartDashboard.getNumber("kVOneD Value: ", kVOneD);
                kAOneD = SmartDashboard.getNumber("kAOneD Value: ", kAOneD);
                kPOneD = SmartDashboard.getNumber("kPOneD Value: ", kPOneD);
                kIOneD = SmartDashboard.getNumber("kIOneD Value: ", kIOneD);
                kDOneD = SmartDashboard.getNumber("kDOneD Value: ", kDOneD);
                kP_thetaOneD = SmartDashboard.getNumber("kP_thetaOneD Value: ", kP_thetaOneD);
 
                kVTwoD = SmartDashboard.getNumber("kVTwoD Value: ", kVTwoD);
                kATwoD = SmartDashboard.getNumber("kATwoD Value: ", kATwoD);
                kPTwoD = SmartDashboard.getNumber("kPTwoD Value: ", kPTwoD);
                kITwoD = SmartDashboard.getNumber("kITwoD Value: ", kITwoD);
                kDTwoD = SmartDashboard.getNumber("kDTwoD Value: ", kDTwoD);
                kP_thetaTwoD = SmartDashboard.getNumber("kP_thetaTwoD Value: ", kP_thetaTwoD);
 
                wheelBaseMultiplier = SmartDashboard.getNumber("Wheel Base Multiplier: ", wheelBaseMultiplier);
 
                DRIVE_RAMP_RATE = new Time(
                        SmartDashboard.getNumber("Drive Ramp Rate: ", DRIVE_RAMP_RATE.get(Time.Unit.SECOND)),
                        Time.Unit.SECOND);
 
                SmartDashboard.putNumber("Left Drive Velocity", getLeftVelocity().get(Distance.Unit.FOOT, Time.Unit.SECOND));
                SmartDashboard.putNumber("Right Drive Velocity", getRightVelocity().get(Distance.Unit.FOOT, Time.Unit.SECOND));

                endX = new Distance(SmartDashboard.getNumber("X End Point: ", 0), Distance.Unit.FOOT);
                endY = new Distance(SmartDashboard.getNumber("Y End Point: ", 0), Distance.Unit.FOOT);
                endAngle = new Angle(SmartDashboard.getNumber("Profile End Angle: ", endAngle.get(Angle.Unit.DEGREE)),
                        Angle.Unit.DEGREE);
                xPoint1 = new Distance(SmartDashboard.getNumber("Point 1 X: ", xPoint1.get(Distance.Unit.FOOT)),
                        Distance.Unit.FOOT);
                yPoint1 = new Distance(SmartDashboard.getNumber("Point 1 Y: ", yPoint1.get(Distance.Unit.FOOT)),
                        Distance.Unit.FOOT);
                anglePoint1 = new Angle(SmartDashboard.getNumber("Point 1 Angle: ", anglePoint1.get(Angle.Unit.DEGREE)),
                        Angle.Unit.DEGREE);
                oneDDrivePercent = SmartDashboard.getNumber("One D Drive Percent: ", 0);
                oneDAccelPercent = SmartDashboard.getNumber("One D Drive Accel Percent: ", 0);
                twoDDrivePercent = SmartDashboard.getNumber("Two D Drive Percent: ", 0);
                twoDAccelPercent = SmartDashboard.getNumber("Two D Drive Accel Percent: ", 0);
                angleToTurn = new Angle(SmartDashboard.getNumber("Angle To Turn: ", 0), Angle.Unit.DEGREE);
 
                profileName = SmartDashboard.getString("Profile Name: ", profileName);

                goalPercent = SmartDashboard.getNumber("Goal Percent", 0);
                leftDriveVelocities.add(getLeftVelocity().get(Distance.Unit.FOOT, Time.Unit.SECOND));
                rightDriveVelocities.add(getRightVelocity().get(Distance.Unit.FOOT, Time.Unit.SECOND));
            }
 
        }
    }
 
    public void periodic() {
        //call iqsuaredc sensor periodic?
        //System.out.println("Left Drive Sensor Position: " + leftDrive.getSelectedSensorPosition());
    }
 
    public void disable() {
        setMotorSpeedInPercent(0,0);
    }
 
    public void startDumbDrive() {
        if (leftDrive != null && rightDrive != null) {
            EnabledSubsystems.DRIVE_DUMB_ENABLED = true;
        }
    }
 
    public void endDumbDrive() {
        if (leftDrive != null && rightDrive != null) {
            EnabledSubsystems.DRIVE_DUMB_ENABLED = false;
        }
    }
 
    public void invertDrive() {

        leftDrive.setInverted(!leftDrive.getInverted());
        leftDriveFollow1.setInverted(!leftDriveFollow1.getInverted());
        leftDriveFollow2.setInverted(!leftDriveFollow2.getInverted());
        rightDrive.setInverted(!rightDrive.getInverted());
        rightDriveFollow1.setInverted(!rightDriveFollow1.getInverted());
        rightDriveFollow2.setInverted(!rightDriveFollow2.getInverted());
    }    
}