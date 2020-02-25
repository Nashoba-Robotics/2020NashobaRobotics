package edu.nr.robotics;

import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.gyro.ResetGyroCommand;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.network.LimelightNetworkTable.Pipeline;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.multicommands.AcquireTargetCommand;
import edu.nr.robotics.multicommands.EmergencyBallStopCommand;
import edu.nr.robotics.multicommands.ProjectileVomitCommand;
import edu.nr.robotics.subsystems.bashbar.ToggleDeployBashBarCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveToBallCommand;
import edu.nr.robotics.subsystems.drive.DriveToSomethingJoystickCommand;
import edu.nr.robotics.subsystems.drive.DriveToTargetCommand;
import edu.nr.robotics.subsystems.drive.DumbDriveToggleCommand;
import edu.nr.robotics.subsystems.drive.EnableSniperForwardMode;
import edu.nr.robotics.subsystems.drive.EnableSniperTurnMode;
import edu.nr.robotics.subsystems.drive.TurnCommand;
import edu.nr.robotics.subsystems.indexer.FireCommand;
import edu.nr.robotics.subsystems.intake.IntakePukeCommand;
import edu.nr.robotics.subsystems.intake.IntakeToggleDeployCommand;
import edu.nr.robotics.subsystems.intake.IntakeSubroutineCommand;
import edu.nr.robotics.subsystems.intake.ToggleRunIntakeCommand;
import edu.nr.robotics.subsystems.shooter.ShooterToggleCommand;
import edu.nr.robotics.subsystems.drive.StayInPlaceDriveCommand;
import edu.nr.lib.commandbased.DoNothingCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.nr.robotics.subsystems.turret.SetTurretLimelightCommand;

public class OI implements SmartDashboardSource {

    public static final double DRIVE_JOYSTICK_DEAD_ZONE = 0.15;
    public static final double JOYSTICK_DEAD_ZONE = 0.3;

    public static final double SPEED_MULTIPLIER = 1.0;

    // Make rteurn to neutral position button
    private static final int CANCEL_ALL_BUTTON_NUMBER = 11; // find all of these and make them too
    // private static final int KID_MODE_SWITCH = 1;

    /*
     * private static final int DRIVE_TO_TARGET_AUTO_NUMBER = 7; private static
     * final int DRIVE_TO_TARGET_HYBRID_NUMBER = 2; private static final int
     * TURN_90_LEFT_NUMBER = 3; private static final int TURN_90_RIGHT_NUMBER = 4;
     * private static final int TURN_180_NUMBER = 2; private static final int
     * TOGGLE_LIMELIGHT_NUMBER = 8; private static final int RESET_GYRO_NUMBER = 10;
     * private static final int SNIPER_MODE_FORWARD = 1; private static final int
     * SNIPER_MODE_TURN = 1; private static final int DUMB_DRIVE_NUMBER = 14;
     * private static final int TOGGLE_KID_MODE_NUMBER = 5;
     */

    private static final int EMERGENCY_MANUAL_SWITCH = 100;
    private static final int ACQUIRE_TARGET_NUMBER = 100;
    private static final int TOGGLE_SHOOTER_NUMBER = 100;
    private static final int FIRE_NUMBER = 100;

    private static final int BASH_BAR_NUMBER = 7;

    private static final int EMERGENCY_SUBSYSTEM_DISABLE_NUMBER = 100;

    private static final int INCREMENT_HOOD_UP_NUMBER = 100;
    private static final int INCREMENT_HOOD_DOWN_NUMBER = 100;
    private static final int ZERO_HOOD_NUMBER = 100;

    private static final int TURRET_LEFT_POSITION_NUMBER = 100;
    private static final int TURRET_CENTER_POSITION_NUMBER = 100;
    private static final int TURRET_RIGHT_POSITION_NUMBER = 100;

    private static final int DEPLOY_CLIMB_LOW = 100;
    private static final int DEPLOY_CLIMB_MID = 100;
    private static final int DEPLOY_CLIMB_HIGH = 100;
    private static final int CLIMB_NUMBER = 100;

    private static final int COLOR_WHEEL_SPIN_NUMBER = 100;
    private static final int COLOR_WHEEL_FIND_COLOR_NUMBER = 100;

    private static final int PUKE_INTAKE_NUMBER = 100;
    private static final int TOGGLE_INTAKE_MOTORS_NUMBER = 100;
    private static final int TOGGLE_INTAKE_DEPLOYED_NUMBER = 100;
    private static final int TOGGLE_INTAKE_ROUTINE_NUMBER = 100;

    private static final int STAY_IN_PLACE_MODE_NUMBER = 100;

    private static final int PUKE_ALL_NUMBER = 100;

    private double driveSpeedMultiplier = 1;

    private static OI singleton;

    public static Joystick driveLeft;
    public static Joystick driveRight;

    private final Joystick operatorLeft;
    private final Joystick operatorRight;

    private final Joystick turretStick;
    private final Joystick hoodStick;
    private final Joystick climbStick;

    private JoystickButton kidModeSwitch;
    private JoystickButton elevGearSwitcherSwitch;

    private static final int STICK_LEFT = 0;
    private static final int STICK_RIGHT = 1;
    private static final int STICK_OPERATOR_LEFT = 2;
    private static final int STICK_OPERATOR_RIGHT = 3;

    private JoystickButton stayInPlaceModeButton;
    private JoystickButton acquireTargetButton;
    private JoystickButton shooterToggleButton;
    private JoystickButton fireButton;

    private JoystickButton emergencyDisableSwitch;

    private JoystickButton emergencyManualSwitch;

    public static final Drive.DriveMode driveMode = Drive.DriveMode.cheesyDrive; // set default type of drive here

    private OI() {
        driveLeft = new Joystick(STICK_LEFT);
        driveRight = new Joystick(STICK_RIGHT);

        operatorLeft = new Joystick(STICK_OPERATOR_LEFT);
        operatorRight = new Joystick(STICK_OPERATOR_RIGHT);

        turretStick = operatorRight;
        hoodStick = operatorLeft;
        climbStick = operatorLeft; // only if climbing and no turret, but figure out later

        // initDriveLeft();
        initDriveRight();

        initOperatorLeft();
        initOperatorRight();

        SmartDashboardSource.sources.add(this);
    }

    public void initDriveLeft() {
        // buttons go here

        // new SetTurretLimelightCommand();
        // end PID loop cancel into velocity PID

        stayInPlaceModeButton = new JoystickButton(driveLeft, STAY_IN_PLACE_MODE_NUMBER);
        stayInPlaceModeButton.whileActiveOnce(new StayInPlaceDriveCommand());
    }

    public void initDriveRight() {
    }

    public void initOperatorLeft() {
        /*
         * emergencyDisableSwitch = new JoystickButton(operatorLeft,
         * EMERGENCY_SUBSYSTEM_DISABLE_NUMBER);
         * emergencyDisableSwitch.whileActiveOnce(new EmergencyBallStopCommand(),
         * false);
         * 
         * acquireTargetButton = new JoystickButton(operatorLeft,
         * ACQUIRE_TARGET_NUMBER); acquireTargetButton.whileActiveOnce(new
         * AcquireTargetCommand(), true);
         * 
         * shooterToggleButton = new JoystickButton(operatorLeft,
         * TOGGLE_SHOOTER_NUMBER); shooterToggleButton.whileActiveOnce(new
         * ShooterToggleCommand(), true);
         * 
         * emergencyManualSwitch = new JoystickButton(operatorLeft,
         * EMERGENCY_MANUAL_SWITCH);
         * 
         * new JoystickButton(operatorLeft, FIRE_NUMBER).whileActiveOnce(new
         * FireCommand(), true);
         * 
         * new JoystickButton(operatorLeft, BASH_BAR_NUMBER).whenPressed(new
         * ToggleDeployBashBarCommand());
         * 
         * new JoystickButton(operatorLeft,
         * TOGGLE_INTAKE_DEPLOYED_NUMBER).whenPressed(new IntakeToggleDeployCommand());
         * 
         * new JoystickButton(operatorLeft, TOGGLE_INTAKE_MOTORS_NUMBER).whenPressed(new
         * ToggleRunIntakeCommand());
         * 
         * new JoystickButton(operatorLeft,
         * TOGGLE_INTAKE_ROUTINE_NUMBER).whenPressed(new IntakeSubroutineCommand());
         * 
         * new JoystickButton(operatorLeft, PUKE_INTAKE_NUMBER).whileActiveOnce(new
         * IntakePukeCommand(), true);
         * 
         * new JoystickButton(operatorLeft, PUKE_ALL_NUMBER).whileActiveOnce(new
         * ProjectileVomitCommand());
         */
    }

    public void initOperatorRight() {

    }

    public static OI getInstance() {
        init();
        return singleton;
    }

    public synchronized static void init() {
        if (singleton == null) {
            singleton = new OI();
        }
    }

    public double getArcadeMoveValue() {
        return -snapDriveJoysticks(driveLeft.getY(), DRIVE_JOYSTICK_DEAD_ZONE) * Drive.MOVE_JOYSTICK_MULTIPLIER
                * SPEED_MULTIPLIER;
    }

    public double getArcadeTurnValue() {
        return -snapDriveJoysticks(driveRight.getX(), DRIVE_JOYSTICK_DEAD_ZONE) * Drive.TURN_JOYSTICK_MULTIPLIER
                * SPEED_MULTIPLIER;
    }

    public double getTankLeftValue() {
        return snapDriveJoysticks(driveLeft.getY(), DRIVE_JOYSTICK_DEAD_ZONE);
    }

    public double getTankRightValue() {
        return snapDriveJoysticks(driveRight.getY(), DRIVE_JOYSTICK_DEAD_ZONE);
    }

    public double getDriveLeftXValue() {
        return snapDriveJoysticks(driveLeft.getX(), DRIVE_JOYSTICK_DEAD_ZONE);
    }

    public double getDriveRightXValue() {
        return snapDriveJoysticks(driveRight.getX(), DRIVE_JOYSTICK_DEAD_ZONE);
    }

    public double getDriveLeftYValue() {
        return snapDriveJoysticks(driveLeft.getY(), DRIVE_JOYSTICK_DEAD_ZONE);
    }

    public double getDriveRightYValue() {
        return snapDriveJoysticks(driveRight.getY(), DRIVE_JOYSTICK_DEAD_ZONE);
    }

    public double getDriveSpeedMultiplier() {
        return driveSpeedMultiplier;
    }

    public void setDriveSpeedMultiplier(double mult) {
        driveSpeedMultiplier = mult;
    }

    private static double snapDriveJoysticks(double value, double deadZone) {
        if (Math.abs(value) < deadZone) {
            value = 0;
        } else if (value > 0) {
            value -= deadZone;
        } else {
            value += deadZone;
        }
        value /= 1 - deadZone;
        return value;
    }

    public double getRawMove() {
        return driveLeft.getY();
    }

    public double getRawTurn() {
        return driveRight.getX();
    }

    public double getTurretTurn() {
        return snapDriveJoysticks(turretStick.getX(), JOYSTICK_DEAD_ZONE);
    }

    public double getHoodTurn() {
        return snapDriveJoysticks(hoodStick.getX(), JOYSTICK_DEAD_ZONE);
    }

    public double getClimbTurn() {
        return snapDriveJoysticks(climbStick.getY(), JOYSTICK_DEAD_ZONE);
    }

    private double getTurnAdjust() {
        // do with buttons
        return 0;
    }

    public void smartDashboardInfo() {
        // add in sensor values for whatever we need
    }

    public boolean isTurretNonZero() {
        return getTurretTurn() != 0;
    }

    public boolean isHoodNonZero() {
        return getHoodTurn() != 0;
    }

    public boolean isTankNonZero() {
        return getTankLeftValue() != 0 || getTankRightValue() != 0;
    }

    public boolean isArcadeNonZero() {
        return getArcadeMoveValue() != 0 || getArcadeTurnValue() != 0;
    }

    public boolean isDriveNonZero() {
        return getDriveLeftXValue() != 0 || getDriveRightXValue() != 0 || getDriveLeftYValue() != 0
                || getDriveRightYValue() != 0;
    }

    public boolean isKidModeOn() {
        // do later if needed
        return false; // kidModeSwitch.get();
    }

    public boolean getManualMode() {
        return emergencyManualSwitch.get();
    }

    public boolean getAcquireTargetHeld() {
        return acquireTargetButton.get();
    }

    public boolean getShooterToggleHeld() {
        return shooterToggleButton.get();
    }
}