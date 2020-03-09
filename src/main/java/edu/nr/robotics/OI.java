package edu.nr.robotics;

import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.gyro.ResetGyroCommand;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.network.LimelightNetworkTable.Pipeline;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.multicommands.AcquireTargetCommand;
import edu.nr.robotics.multicommands.ClimbCommand;
import edu.nr.robotics.multicommands.EmergencyBallStopCommand;
import edu.nr.robotics.multicommands.PointBlankShotCommand;
import edu.nr.robotics.multicommands.ProjectileVomitCommand;
import edu.nr.robotics.multicommands.ShootCommand;
import edu.nr.robotics.multicommands.SwitchLimelightFeed;
import edu.nr.robotics.subsystems.bashbar.ToggleDeployBashBarCommand;
import edu.nr.robotics.subsystems.climbdeploy.ClimbDeploy;
import edu.nr.robotics.subsystems.climbdeploy.ClimbDeployCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveToBallCommand;
import edu.nr.robotics.subsystems.drive.DriveToSomethingJoystickCommand;
import edu.nr.robotics.subsystems.drive.DriveToTargetCommand;
import edu.nr.robotics.subsystems.drive.DumbDriveToggleCommand;
import edu.nr.robotics.subsystems.drive.EnableSniperForwardMode;
import edu.nr.robotics.subsystems.drive.EnableSniperMode;
import edu.nr.robotics.subsystems.drive.InvertDriveCommand;
import edu.nr.robotics.subsystems.drive.TurnCommand;
import edu.nr.robotics.subsystems.hood.HoodDownButtonCommand;
import edu.nr.robotics.subsystems.hood.HoodUpButtonCommand;
import edu.nr.robotics.subsystems.hood.ZeroHoodCommand;
import edu.nr.robotics.subsystems.indexer.FireCommand;
import edu.nr.robotics.subsystems.indexer.FireOneCommand;
import edu.nr.robotics.subsystems.indexer.IndexerSetVelocitySmartDashboardCommand;
import edu.nr.robotics.subsystems.intake.IntakePukeCommand;
import edu.nr.robotics.subsystems.intake.IntakeToggleDeployCommand;
import edu.nr.robotics.subsystems.intake.IntakeSubroutineCommand;
import edu.nr.robotics.subsystems.intake.ToggleRunIntakeCommand;
import edu.nr.robotics.subsystems.shooter.ShooterToggleCommand;
import edu.nr.robotics.subsystems.transfer.ZeroBallCountCommand;
import edu.nr.robotics.subsystems.transferhook.TransferHook;
import edu.nr.robotics.subsystems.drive.StayInPlaceDriveCommand;
import edu.nr.lib.commandbased.DoNothingCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.nr.robotics.subsystems.turret.SetTurretCenterCommand;
import edu.nr.robotics.subsystems.turret.SetTurretLimelightCommand;
import edu.nr.robotics.subsystems.turret.TurretLeftButtonCommand;
import edu.nr.robotics.subsystems.turret.TurretRightButtonCommand;
import edu.nr.robotics.subsystems.turret.setTurretLeftCommand;
import edu.nr.robotics.subsystems.turret.setTurretRightCommand;

public class OI implements SmartDashboardSource {

    public static final double DRIVE_JOYSTICK_DEAD_ZONE = 0.3;
    public static final double JOYSTICK_DEAD_ZONE = 0.5;

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

    //private static final int SNIPER_MODE_NUMBER = 2;

    private static final int EMERGENCY_MANUAL_SWITCH = 1;
    private static final int ACQUIRE_TARGET_NUMBER = 2; 
    private static final int TOGGLE_SHOOTER_NUMBER = 3;
    private static final int FIRE_NUMBER = 10;
    private static final int FIRE_ONE_NUMBER = 11;
    private static final int POINT_BLANK_SHOT_NUMBER = 16;

    //private static final int BASH_BAR_NUMBER = 7;

    private static final int EMERGENCY_SUBSYSTEM_DISABLE_NUMBER = 2;

    private static final int INCREMENT_HOOD_UP_NUMBER = 4;
    private static final int INCREMENT_HOOD_DOWN_NUMBER = 6;
    private static final int ZERO_BALL_COUNT_NUMBER = 5;

    private static final int TURRET_LEFT_POSITION_NUMBER = 100;
    private static final int TURRET_CENTER_POSITION_NUMBER = 100;
    private static final int TURRET_RIGHT_POSITION_NUMBER = 12;

    private static final int DEPLOY_CLIMB_LOW = 10;
    private static final int DEPLOY_CLIMB_MID = 9;
    private static final int DEPLOY_CLIMB_HIGH = 7;
    private static final int CLIMB_NUMBER = 2;

    //private static final int COLOR_WHEEL_SPIN_NUMBER = 100;
    //private static final int COLOR_WHEEL_FIND_COLOR_NUMBER = 100;

    private static final int PUKE_INTAKE_NUMBER = 6;
    private static final int TOGGLE_INTAKE_MOTORS_NUMBER = 3;
    private static final int TOGGLE_INTAKE_DEPLOYED_NUMBER = 5;
    private static final int TOGGLE_INTAKE_ROUTINE_NUMBER = 1;

    //private static final int STAY_IN_PLACE_MODE_NUMBER = 1;

    private static final int PUKE_ALL_NUMBER = 4;

    private double driveSpeedMultiplier = 1;

    private static OI singleton;

    public static Joystick driveLeft;
    public static Joystick driveRight;

    private final Joystick operatorLeft;
    private final Joystick operatorRight;

    private final Joystick turretStick;
    //private final Joystick hoodStick;
    private final Joystick climbStick;
    //private final Joystick transferHookStick;

    public static boolean climbMode = false;

    private JoystickButton kidModeSwitch;
    private JoystickButton elevGearSwitcherSwitch;

    private static final int STICK_LEFT = 0;
    private static final int STICK_RIGHT = 1;
    private static final int STICK_OPERATOR_LEFT = 2;
    private static final int STICK_OPERATOR_RIGHT = 3;

    private JoystickButton acquireTargetButton;
    private JoystickButton shooterToggleButton;
    private JoystickButton pointBlankButton;

    private JoystickButton emergencyDisableSwitch;

    private JoystickButton emergencyManualSwitch;

    public static final Drive.DriveMode driveMode = Drive.DriveMode.cheesyDrive; // set default type of drive here

    private OI() {
        driveLeft = new Joystick(STICK_LEFT);
        driveRight = new Joystick(STICK_RIGHT);

        operatorLeft = new Joystick(STICK_OPERATOR_LEFT);
        operatorRight = new Joystick(STICK_OPERATOR_RIGHT);

        turretStick = operatorRight;
        //transferHookStick = operatorRight;
        //hoodStick = operatorLeft;
        climbStick = operatorLeft; // only if climbing and no turret, but figure out later

        initDriveLeft();
        initDriveRight();

        initOperatorLeft();
        initOperatorRight();

        SmartDashboardSource.sources.add(this);
    }

    public void initDriveLeft() {

        new JoystickButton(driveLeft, 14).whenPressed(new ZeroBallCountCommand());

        new JoystickButton(driveLeft, 1).whileActiveOnce(new EnableSniperMode());
        //new JoystickButton(driveLeft, SNIPER_MODE_NUMBER).whileActiveOnce(new EnableSniperMode());
        // buttons go here

        // new SetTurretLimelightCommand();
        // end PID loop cancel into velocity PID

        //stayInPlaceModeButton = new JoystickButton(driveLeft, STAY_IN_PLACE_MODE_NUMBER);
        //stayInPlaceModeButton.whileActiveOnce(new StayInPlaceDriveCommand());
        //new JoystickButton(driveLeft, 1).whenPressed(new IndexerSetVelocitySmartDashboardCommand());

        //new JoystickButton(driveLeft, 3).whileActiveOnce(new SetTurretLimelightCommand(), true);
        
        //new JoystickButton(driveLeft, 1).whileActiveOnce(new StayInPlaceDriveCommand());
    }

    public void initDriveRight() {
        //new JoystickButton(driveRight, 1).whileActiveOnce(new ShootCommand(), true);
        pointBlankButton = new JoystickButton(driveRight, POINT_BLANK_SHOT_NUMBER);
        pointBlankButton.whileActiveOnce(new PointBlankShotCommand());
    }

    public void initOperatorLeft() {
        new JoystickButton(operatorLeft, PUKE_ALL_NUMBER).whileActiveOnce(new ProjectileVomitCommand());  
        new JoystickButton(operatorLeft, 12).whenPressed(new ZeroHoodCommand()); // trench prep
        new JoystickButton(operatorLeft, TOGGLE_INTAKE_ROUTINE_NUMBER).whenPressed(new IntakeSubroutineCommand());
        new JoystickButton(operatorLeft, TOGGLE_INTAKE_DEPLOYED_NUMBER).whenPressed(new IntakeToggleDeployCommand());
        new JoystickButton(operatorLeft, PUKE_INTAKE_NUMBER).whileActiveOnce(new IntakePukeCommand(), true);

        new JoystickButton(operatorLeft, TOGGLE_INTAKE_MOTORS_NUMBER).whenPressed(new ToggleRunIntakeCommand());
        new JoystickButton(operatorLeft, 11).whileActiveOnce(new TurretLeftButtonCommand());
        new JoystickButton(operatorLeft, 9).whenPressed(new SetTurretCenterCommand());

        new JoystickButton(operatorLeft, FIRE_NUMBER).whileActiveOnce(new FireCommand(), true);

        acquireTargetButton = new JoystickButton(operatorLeft, ACQUIRE_TARGET_NUMBER); 
        acquireTargetButton.whileActiveOnce(new AcquireTargetCommand(), true);

        //new JoystickButton(operatorLeft, ZERO_HOOD_NUMBER).whenPressed(new ZeroHoodCommand());
    }

    public void initOperatorRight() {
        //new JoystickButton(operatorRight, 1).whileActiveOnce(new IntakePukeCommand(), true);
        new JoystickButton(operatorRight, FIRE_ONE_NUMBER).whileActiveOnce(new FireOneCommand(), true);
        new JoystickButton(operatorRight, INCREMENT_HOOD_UP_NUMBER).whileActiveOnce(new HoodUpButtonCommand());
        new JoystickButton(operatorRight, INCREMENT_HOOD_DOWN_NUMBER).whileActiveOnce(new HoodDownButtonCommand());

        emergencyManualSwitch = new JoystickButton(operatorRight, EMERGENCY_MANUAL_SWITCH);
        emergencyManualSwitch.whileActiveOnce(new SwitchLimelightFeed());
        new JoystickButton(operatorRight, EMERGENCY_SUBSYSTEM_DISABLE_NUMBER).whileActiveOnce(new EmergencyBallStopCommand(), false);
        
        new JoystickButton(operatorRight, TOGGLE_SHOOTER_NUMBER).whenPressed(new ShooterToggleCommand());

        new JoystickButton(operatorRight, ZERO_BALL_COUNT_NUMBER).whenPressed(new ZeroBallCountCommand());

        new JoystickButton(operatorRight, TURRET_RIGHT_POSITION_NUMBER).whileActiveOnce(new TurretRightButtonCommand());
        
        new JoystickButton(operatorRight, CLIMB_NUMBER).whileActiveOnce(new ClimbCommand(), true);
        /*
        //new JoystickButton(operatorRight, 3).whenPressed(new ToggleRunIntakeCommand());
        */
        
        new JoystickButton(operatorRight, DEPLOY_CLIMB_LOW).whenPressed(new ClimbDeployCommand(ClimbDeploy.LOW_DISTANCE));
        new JoystickButton(operatorRight, DEPLOY_CLIMB_MID).whenPressed(new ClimbDeployCommand(ClimbDeploy.MID_DISTANCE));
        new JoystickButton(operatorRight, DEPLOY_CLIMB_HIGH).whenPressed(new ClimbDeployCommand(ClimbDeploy.HIGH_DISTANCE));

        new JoystickButton(operatorRight, 8).whileActiveOnce(new ClimbCommand());
        

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
        //System.out.println("snapdrive joysticks value in + " + value);
        if (Math.abs(value) < deadZone) {
            value = 0;
        } else if (value > 0) {
            value -= deadZone;
        } else {
            value += deadZone;
        }
        value /= 1 - deadZone;
        //System.out.println("snapdrive joysticks value out + " + value);
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

    public double getOperatorLeftTurn() {
        return snapDriveJoysticks(operatorLeft.getX(), 0.5);
    }

    /*
    public double getTransferHookTurn()
    {
        return snapDriveJoysticks(transferHookStick.getX(), JOYSTICK_DEAD_ZONE);
    }
    */

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

    public boolean isOperatorLeftNonZero() {
        return getOperatorLeftTurn() != 0;
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
        if(emergencyManualSwitch.get() == true)
        {
            LimelightNetworkTable.getInstance().setPipeline(Pipeline.DriverCam);
        }
        return emergencyManualSwitch.get();
    }

    public boolean getAcquireTargetHeld() {
        return acquireTargetButton.get();
    }

    public boolean getShooterToggleHeld() {
        return shooterToggleButton.get();
    }

    public boolean getPointBlank()
    {
        return pointBlankButton.get();
    }
}