package edu.nr.robotics.subsystems.turret;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turret extends NRSubsystem {
    private static Turret singleton;

    private TalonSRX turretTalon;

    public static final double ENCODER_TICKS_PER_DEGREE_TURRET = (4096.0 / 360);
    private static double F_POS_TURRET = 0.1;
    private static double P_POS_TURRET = 04; // 0.2 before this
    private static double I_POS_TURRET = 0;
    private static double D_POS_TURRET = 0;

    private static double turretMaxOutput = 1;

    private static Angle setAngle = Angle.ZERO;
    public static Angle goalAngle = Angle.ZERO;
    public static Angle deltaAngle = Angle.ZERO;

    public static Angle LEFT_LIMIT = new Angle(-45, Angle.Unit.DEGREE);
    public static Angle RIGHT_LIMIT = new Angle(45, Angle.Unit.DEGREE);

    public static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;
    // Type of PID. 0 = primary. 1 = cascade
    public static final int PID_TYPE = 0;

    public static final int POS_SLOT = 0;

    public static final int DEFAULT_TIMEOUT = 0;

    public static double goalPercent;

    private Turret() {
        if (EnabledSubsystems.TURRET_ENABLED) {
            turretTalon = new TalonSRX(RobotMap.TURRET_TALON);

            turretTalon.config_kF(POS_SLOT, F_POS_TURRET);
            turretTalon.config_kP(POS_SLOT, P_POS_TURRET);
            turretTalon.config_kI(POS_SLOT, I_POS_TURRET);
            turretTalon.config_kD(POS_SLOT, D_POS_TURRET);

            turretTalon.setNeutralMode(NEUTRAL_MODE);

            turretTalon.setInverted(false);

            turretTalon.setSensorPhase(true);

            turretTalon.setSelectedSensorPosition(0);

            if (EnabledSubsystems.TURRET_DUMB_ENABLED) {
                turretTalon.set(ControlMode.PercentOutput, 0);
            } else {
                turretTalon.set(ControlMode.Position, getAngle().get(Angle.Unit.TURRET_ENCODER_TICK));
            }
            smartDashboardInit();
        }
    }

    public synchronized static void init() {
        if (singleton == null)
            singleton = new Turret();
    }

    public static Turret getInstance() {
        if (singleton == null)
            init();
        return singleton;
    }

    public void disable() {
        if (turretTalon != null) {
            turretTalon.set(ControlMode.PercentOutput, 0);
        }
    }

    public void smartDashboardInit() {
        if (EnabledSubsystems.TURRET_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putNumber("P_POS_TURRET", P_POS_TURRET);
            SmartDashboard.putNumber("I_POS_TURRET", I_POS_TURRET);
            SmartDashboard.putNumber("D_POS_TURRET", D_POS_TURRET);
            SmartDashboard.putNumber("F_POS_TURRET", F_POS_TURRET);

            SmartDashboard.putNumber("Turret Current", getCurrent());

            SmartDashboard.putNumber("Set Angle Turret", setAngle.get(Angle.Unit.DEGREE));
            SmartDashboard.putNumber("Turret Current Angle", getAngle().get(Angle.Unit.DEGREE));
            SmartDashboard.putNumber("Turret Goal Angle", goalAngle.get(Angle.Unit.DEGREE));
            SmartDashboard.putNumber("SET TURRET PERCENT", 0);

            SmartDashboard.putBoolean("Lim Turret Left", EnabledSensors.getInstance().LimTurretLeft.get());
            SmartDashboard.putBoolean("Lim Turret Right", EnabledSensors.getInstance().LimTurretRight.get());
            SmartDashboard.putNumber("Turret Actual Speed", getActualSpeed().get(Angle.Unit.DEGREE, Time.Unit.SECOND));

            SmartDashboard.putNumber("Turret Encoder Position", turretTalon.getSelectedSensorPosition());

            SmartDashboard.putNumber("Turret Limelight Angle", SetTurretLimelightCommand.limeLightAngle.get(Angle.Unit.DEGREE));
        }
    }

    public void smartDashboardInfo() {
        if (turretTalon != null) {
            if (EnabledSubsystems.TURRET_SMARTDASHBOARD_DEBUG_ENABLED) {

                SmartDashboard.putNumber("Turret Limelight Angle", SetTurretLimelightCommand.limeLightAngle.get(Angle.Unit.DEGREE));

                F_POS_TURRET = SmartDashboard.getNumber("F_POS_TURRET", F_POS_TURRET);
                P_POS_TURRET = SmartDashboard.getNumber("P_POS_TURRET", P_POS_TURRET);
                I_POS_TURRET = SmartDashboard.getNumber("I_POS_TURRET", I_POS_TURRET);
                D_POS_TURRET = SmartDashboard.getNumber("D_POS_TURRET", D_POS_TURRET);

                turretTalon.config_kF(POS_SLOT, F_POS_TURRET);
                turretTalon.config_kP(POS_SLOT, P_POS_TURRET);
                turretTalon.config_kI(POS_SLOT, I_POS_TURRET);
                turretTalon.config_kD(POS_SLOT, D_POS_TURRET);

                SmartDashboard.putNumber("Turret Current", getCurrent());

                goalAngle = new Angle(SmartDashboard.getNumber("Turret Goal Angle", goalAngle.get(Angle.Unit.DEGREE)),
                        Angle.Unit.DEGREE);
                SmartDashboard.putNumber("Set Angle Turret", setAngle.get(Angle.Unit.DEGREE));
                SmartDashboard.putNumber("Turret Current Angle", getAngle().get(Angle.Unit.TURRET_ENCODER_TICK));
                goalPercent = SmartDashboard.getNumber("SET TURRET PERCENT", 0);

                SmartDashboard.putBoolean("Lim Turret Left", EnabledSensors.getInstance().LimTurretLeft.get());
                SmartDashboard.putBoolean("Lim Turret Right", EnabledSensors.getInstance().LimTurretRight.get());

                SmartDashboard.putNumber("Turret Actual Speed",
                        getActualSpeed().get(Angle.Unit.DEGREE, Time.Unit.SECOND));

                SmartDashboard.putNumber("Turret Encoder Position", turretTalon.getSelectedSensorPosition());
            }
        }
    }

    public Angle getAngle() {
        if (turretTalon != null) {
            return new Angle(turretTalon.getSelectedSensorPosition(), Angle.Unit.TURRET_ENCODER_TICK);
        }
        return Angle.ZERO;
    }

    public AngularSpeed getActualSpeed() {
        if (turretTalon != null) {
            return new AngularSpeed(turretTalon.getSelectedSensorVelocity(), Angle.Unit.TURRET_ENCODER_TICK,
                    Time.Unit.HUNDRED_MILLISECOND);
        }

        return AngularSpeed.ZERO;
    }

    public void setAngle(Angle targetAngle) {
        if (turretTalon != null) {
            setAngle = targetAngle;
            turretTalon.selectProfileSlot(POS_SLOT, PID_TYPE);
            turretTalon.set(ControlMode.Position, setAngle.get(Angle.Unit.TURRET_ENCODER_TICK));
        }
    }

    public void periodic() {
        if (EnabledSubsystems.TURRET_ENABLED) {
            if (EnabledSensors.getInstance().LimTurretRight.get()) {
                if (getActualSpeed().get(Angle.Unit.DEGREE, Time.Unit.SECOND) < 0) {
                    setMotorSpeedInPercent(0);
                    setAngle(RIGHT_LIMIT);
                }
            }
            if (EnabledSensors.getInstance().LimTurretLeft.get()) {
                if (getActualSpeed().get(Angle.Unit.DEGREE, Time.Unit.SECOND) > 0) {
                    setMotorSpeedInPercent(0);
                    setAngle(LEFT_LIMIT);
                }
            }
        }
    }

    public double getCurrent() {
        if (turretTalon != null) {
            return turretTalon.getStatorCurrent();
        }
        return 0;
    }

    public void setMotorSpeedInPercent(double percent) {
        if (turretTalon != null) {
            if (percent < turretMaxOutput)
                turretTalon.set(ControlMode.PercentOutput, percent);
        }
    }

    public void zeroEncoder()
    {
        turretTalon.setSelectedSensorPosition(0);
    }
}