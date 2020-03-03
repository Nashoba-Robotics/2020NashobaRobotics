package edu.nr.robotics.subsystems.transfer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularAcceleration;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Transfer extends NRSubsystem {
    private static Transfer singleton;

    private VictorSPX transferVictor;

    public static final int VOLTAGE_COMPENSATION_LEVEL = 12;
    public static final double MIN_MOVE_VOLTAGE = 0.0;
    public static final int DEFAULT_TIMEOUT = 0;
    public static final double PUKE_PERCENT = -1;
    public static final double TRANSFER_PERCENT = 0.42; // more for real
    public static final Time PUKE_TIME = new Time(1, Time.Unit.SECOND);

    public PowerDistributionPanel pdp;

    public static double F_POS_TRANSFER = 0;
    public static double P_POS_TRANSFER = 0;
    public static double I_POS_TRANSFER = 0;
    public static double D_POS_TRANSFER = 0;

    public static double F_VEL_TRANSFER = 0;
    public static double P_VEL_TRANSFER = 0;
    public static double I_VEL_TRANSFER = 0;
    public static double D_VEL_TRANSFER = 0;

    public static final double PROFILE_VEL_PERCENT_TRANSFER = 0.6; // change
    public static final double PROFILE_ACCEL_PERCENT_TRANSFER = 0.6;
    public static final int PEAK_CURRENT_TRANSFER = 60;
    public static final int CONTINUOUS_CURRENT_LIMIT_TRANSFER = 40;

    public static final NeutralMode NEUTRAL_MODE_TRANSFER = NeutralMode.Brake;

    public static final int PID_TYPE = 0;
    public static final int VEL_SLOT = 0;
    public static final int POS_SLOT = 1;

    public static Time VOLTAGE_RAMP_RATE_TRANSFER = new Time(0.05, Time.Unit.SECOND);

    public static final AngularSpeed MAX_SPEED_TRANSFER = new AngularSpeed(2000, Angle.Unit.ROTATION, Time.Unit.SECOND);
    public static final AngularAcceleration MAX_ACCEL_TRANSFER = new AngularAcceleration(2000, Angle.Unit.ROTATION,
            Time.Unit.SECOND, Time.Unit.SECOND);

    public static final double ENCODER_TICKS_PER_INCH_BALL_MOVED = 400; // not really... have to change this one
    public static final double ENCODER_TICKS_PER_DEGREE = 2048.0 / 360;
    public static final double ENCODER_TICKS_PER_DEGREE_SPARK = 42.0 / 360;

    public static Distance distanceSetPoint = Distance.ZERO;
    public static Distance deltaDistance = Distance.ZERO;
    public static AngularSpeed speedSetPoint = AngularSpeed.ZERO;
    public static final Distance TRANSFER_DISTANCE = new Distance(8, Distance.Unit.INCH); // change for real robot

    public static double goalSpeed = 0;

    public static final Time TRANSFER_TIME = new Time(1, Time.Unit.SECOND);
    public static final Time TRANSFER_ALL_TIME = new Time(0.1, Time.Unit.SECOND);

    public static int TRANSFER_THRESHOLD = 650;

    public static double goalPercent = 0.0;
    // need sensors for have a ball, tune transfer percent and time for transfer
    // command

    public double falseTimer = 0;
    public double currentTimer = 0;

    private boolean previousSensorValue = false;

    public static int ballCount = 0;

    private Transfer() {
        if (EnabledSubsystems.TRANSFER_ENABLED) {

            transferVictor = new VictorSPX(RobotMap.TRANSFER_VICTOR);

            transferVictor.setNeutralMode(NeutralMode.Brake);
            transferVictor.setInverted(false);

            pdp = new PowerDistributionPanel(RobotMap.PDP_ID);

            if (EnabledSubsystems.TRANSFER_DUMB_ENABLED) {

                transferVictor.set(ControlMode.PercentOutput, 0);
            } else {
                transferVictor.set(ControlMode.PercentOutput, 0);
            }
        }
        smartDashboardInit();
    }

    public static Transfer getInstance() {
        if (singleton == null) {
            init();
        }
        return singleton;
    }

    public synchronized static void init() {
        if (singleton == null) {
            singleton = new Transfer();
            // singleton.setDefaultCommand(new TransferProcedureCommand());
        }
    }

    public void disable() {
        if (transferVictor != null) {
            transferVictor.set(ControlMode.PercentOutput, 0);
        }
    }

    public void smartDashboardInit() {
        if (EnabledSubsystems.TRANSFER_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putNumber("Transfer Percent", 0);

            SmartDashboard.putNumber("F_POS_TRANSFER: ", F_POS_TRANSFER);
            SmartDashboard.putNumber("P_POS_TRANSFER: ", P_POS_TRANSFER);
            SmartDashboard.putNumber("I_POS_TRANSFER: ", I_POS_TRANSFER);
            SmartDashboard.putNumber("D_POS_TRANSFER: ", D_POS_TRANSFER);

            SmartDashboard.putNumber("F_VEL_TRANSFER: ", F_VEL_TRANSFER);
            SmartDashboard.putNumber("P_VEL_TRANSFER: ", P_VEL_TRANSFER);
            SmartDashboard.putNumber("I_VEL_TRANSFER: ", I_VEL_TRANSFER);
            SmartDashboard.putNumber("D_VEL_TRANSFER: ", D_VEL_TRANSFER);
            if (transferVictor != null) {
                SmartDashboard.putNumber("Transfer Bus Voltage: ", transferVictor.getBusVoltage());
                SmartDashboard.putNumber("Transfer Victor Output Percent: ", transferVictor.getMotorOutputPercent());
            }
            SmartDashboard.putNumber("Transfer Set Position", distanceSetPoint.get(Distance.Unit.INCH));
            SmartDashboard.putNumber("Transfer Delta Position", deltaDistance.get(Distance.Unit.INCH));
            SmartDashboard.putNumber("Transfer Goal Speed", goalSpeed);

            SmartDashboard.putNumber("Transfer Goal Percent", 0);
        }
    }

    public void smartDashboardInfo() {
        if (EnabledSubsystems.TRANSFER_SMARTDASHBOARD_DEBUG_ENABLED) {
            //setMotorSpeedInPercent(SmartDashboard.getNumber("Transfer Percent", 0));

            F_POS_TRANSFER = SmartDashboard.getNumber("F_POS_TRANSFER: ", F_POS_TRANSFER);
            P_POS_TRANSFER = SmartDashboard.getNumber("P_POS_TRANSFER: ", P_POS_TRANSFER);
            I_POS_TRANSFER = SmartDashboard.getNumber("I_POS_TRANSFER: ", I_POS_TRANSFER);
            D_POS_TRANSFER = SmartDashboard.getNumber("D_POS_TRANSFER: ", D_POS_TRANSFER);

            F_VEL_TRANSFER = SmartDashboard.getNumber("F_VEL_TRANSFER: ", F_VEL_TRANSFER);
            P_VEL_TRANSFER = SmartDashboard.getNumber("P_VEL_TRANSFER: ", P_VEL_TRANSFER);
            I_VEL_TRANSFER = SmartDashboard.getNumber("I_VEL_TRANSFER: ", I_VEL_TRANSFER);
            D_VEL_TRANSFER = SmartDashboard.getNumber("D_VEL_TRANSFER: ", D_VEL_TRANSFER);

            if (transferVictor != null) {
                SmartDashboard.putNumber("Transfer Bus Voltage: ", transferVictor.getBusVoltage());
                SmartDashboard.putNumber("Transfer Victor Output Percent: ", transferVictor.getMotorOutputPercent());
            }

            goalSpeed = SmartDashboard.getNumber("Transfer Goal Speed", goalSpeed);

            goalPercent = SmartDashboard.getNumber("Tansfer Goal Percent", 0);

            distanceSetPoint = new Distance(SmartDashboard.getNumber("Transfer Set Position", distanceSetPoint.get(Distance.Unit.INCH)), Distance.Unit.INCH);
            deltaDistance = new Distance(SmartDashboard.getNumber("Transfer Delta Position", deltaDistance.get(Distance.Unit.INCH)), Distance.Unit.INCH);
        }
    }

    public double getOutputCurrent()
    {
        if(transferVictor != null)
        {
            return pdp.getCurrent(RobotMap.TRANSFER_VICTOR);
        }
        return 0.0;
    }

    public double getEncoderPosition() {
        if (transferVictor != null) {
            return transferVictor.getSelectedSensorPosition();
        }

        return 0;
    }

    public void setMotorSpeedInPercent(double percent) {
        if (transferVictor != null) {
            transferVictor.set(ControlMode.PercentOutput, percent);
        }
    }

    public AngularSpeed getSpeed() {
        if (transferVictor != null) {
            return new AngularSpeed(transferVictor.getSelectedSensorVelocity(), Angle.Unit.TRANSFER_ENCODER_TICK,
                    Time.Unit.HUNDRED_MILLISECOND);
        }

        return AngularSpeed.ZERO;
    }

    public void setSpeed(AngularSpeed targetSpeed) 
    { 

    }

    public void periodic() {
        if(EnabledSubsystems.TRANSFER_ENABLED)
        {
            if(EnabledSensors.getInstance().transferSensor.get() == false) {
                if(!(EnabledSensors.getInstance().transferSensor.get() == previousSensorValue)) {
                    Transfer.getInstance().incrementBallCount();
                }
            }
            previousSensorValue = EnabledSensors.getInstance().transferSensor.get();
        }
    }

    public boolean hasBall() {
        if (EnabledSubsystems.TRANSFER_ENABLED) {
            if(!EnabledSensors.getInstance().transferSensor.get())
            {
                falseTimer = Timer.getFPGATimestamp();
            }
            else if(EnabledSensors.getInstance().transferSensor.get())
            {
                currentTimer = Timer.getFPGATimestamp();
                if(currentTimer - falseTimer >= .1)
                    return true;
            }
            //return EnabledSensors.getInstance().transferSensor.get();
        }
        return false;
    }

    public void neutralOutput() {
        transferVictor.neutralOutput();
    }

    public int getNumberOfBalls() {
        return Transfer.ballCount;
    }

    public void incrementBallCount() {
        if(Transfer.ballCount < 4)
            Transfer.ballCount++;
    }

    public void decrementBallCount() {
        if(Transfer.ballCount > 0)
            Transfer.ballCount--;
    }
}