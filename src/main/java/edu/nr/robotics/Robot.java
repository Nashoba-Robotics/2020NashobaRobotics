package edu.nr.robotics;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.nr.lib.motorcontrollers.SparkMax;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.ietf.jgss.Oid; // what is this thing??? why? 

import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.network.LimelightNetworkTable.Pipeline;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Time.Unit;
import edu.nr.robotics.auton.automaps.DirectlyInFrontOfTrenchCommand;
import edu.nr.robotics.auton.automaps.JustShootCommand;
import edu.nr.robotics.auton.automaps.MiddleOfNowhereCommand;
import edu.nr.robotics.auton.automaps.MiddleToThreeRendezvousAutoCommand;
import edu.nr.robotics.auton.automaps.MiddleToTwoRendezvousCommand;
import edu.nr.robotics.auton.automaps.SimpleMiddleAutoCommand;
import edu.nr.robotics.auton.autoroutes.AutoChoosers;
import edu.nr.robotics.auton.autoroutes.AutoChoosers.ballLocation;
import edu.nr.robotics.auton.autoroutes.AutoChoosers.startPos;
import edu.nr.robotics.subsystems.indexer.IndexingProcedureCommand;
import edu.nr.robotics.multicommands.ProjectileVomitCommand;
import edu.nr.robotics.multicommands.States;
import edu.nr.robotics.multicommands.States.State;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.bashbar.ToggleDeployBashBarCommand;
import edu.nr.robotics.subsystems.climbdeploy.ClimbDeploy;
import edu.nr.robotics.subsystems.climbdeploy.ClimbDeployJoystickCommand;
import edu.nr.robotics.subsystems.climbdeploy.ClimbDeploySmartDashboardCommand;
import edu.nr.robotics.subsystems.colorwheel.ColorWheel;
import edu.nr.robotics.subsystems.colorwheel.ColorWheelRotateCommand;
import edu.nr.robotics.subsystems.colorwheel.TargetColorCommand;
import edu.nr.robotics.subsystems.colorwheel.ToggleColorWheelSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.CSVSaverDisable;
import edu.nr.robotics.subsystems.drive.CSVSaverEnable;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveForwardBasicSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.DriveToBallCommand;
import edu.nr.robotics.subsystems.drive.EnableMotionProfile;
import edu.nr.robotics.subsystems.drive.EnableMotionProfileSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.EnableReverseTwoDMotionProfileSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.EnableTwoDMotionProfileSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.TurnSmartDashboardCommand;
import edu.nr.robotics.subsystems.hood.DeltaHoodAngleSmartDashboardCommand;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodPercentCommand;
import edu.nr.robotics.subsystems.hood.SetHoodAngleSmartDashboardCommand;
import edu.nr.robotics.subsystems.transfer.Transfer;
import edu.nr.robotics.subsystems.transfer.TransferCommand;
import edu.nr.robotics.subsystems.transfer.TransferProcedureCommand;
import edu.nr.robotics.subsystems.transferhook.TransferHook;
import edu.nr.robotics.subsystems.transferhook.TransferHookJoystickCommand;
import edu.nr.robotics.subsystems.indexer.IndexerDeltaPositionSmartDashboardCommand;
import edu.nr.robotics.subsystems.indexer.IndexerPukeCommand;
import edu.nr.robotics.subsystems.indexer.IndexerSetVelocityCommand;
import edu.nr.robotics.subsystems.indexer.IndexerSetVelocitySmartDashboardCommand;
import edu.nr.robotics.subsystems.indexer.IndexingProcedureCommand;
import edu.nr.robotics.subsystems.intake.Intake;
import edu.nr.robotics.subsystems.sensors.DigitalSensor;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
//import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.nr.robotics.subsystems.sensors.ISquaredCSensor;
import edu.nr.robotics.subsystems.shooter.SetShooterSpeedSmartDashboardCommand;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.turret.DeltaTurretAngleSmartDashboardCommand;
import edu.nr.robotics.subsystems.turret.SetTurretAngleSmartDashboardCommand;
import edu.nr.robotics.subsystems.turret.SetTurretLimelightCommand;
import edu.nr.robotics.subsystems.turret.SetTurretPercentCommand;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.nr.robotics.subsystems.turret.TurretLimelightCommand;
import edu.nr.robotics.subsystems.turret.ZeroTurretEncoderCommand;
import edu.nr.robotics.subsystems.winch.SetWinchPositionCommand;
import edu.nr.robotics.subsystems.winch.WinchClimbRetractCommand;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.indexer.IndexerDeltaPositionCommand;
import edu.nr.robotics.subsystems.indexer.IndexerDeltaPositionSmartDashboardCommand;
import edu.nr.robotics.subsystems.indexer.IndexerSetVelocityCommand;
import edu.nr.robotics.subsystems.indexer.IndexerSetVelocitySmartDashboardCommand;

/*import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;*/
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

    private static Robot singleton;

    private static double period = 0.02;

    private PWMSparkMax testSpark = new PWMSparkMax(RobotMap.HOOD_SPARK);

    double dt;
    double dtTot = 0;
    int count = 0;

    private double prevTime = 0;

    private Command autonomousCommand;

    public AutoChoosers.startPos selectedStartPos;
    public AutoChoosers.ballLocation selectedBallLocation;

    public double autoWaitTime;
    public Compressor robotCompressor;

    public synchronized static Robot getInstance() {
        return singleton;
    }

    public void robotInit() {
        singleton = this;

        m_period = period; // period that the code runs at

        //autoChooserInit();
        //GameData.init();
        //ColorWheel.init();

        OI.init();
        //Winch.init();
        //ClimbDeploy.init();
        Drive.init();
        //Turret.init();
        //Shooter.init();
        //Hood.init();
        //Intake.init();
        //Indexer.init();
        //Transfer.init();

        //robotCompressor = new Compressor(RobotMap.PCM_ID);
        //robotCompressor.start();

        smartDashboardInit();
        // CameraInit();

        LimelightNetworkTable.getInstance().lightLED(true);
        LimelightNetworkTable.getInstance().setPipeline(Pipeline.Target);

        
        if(EnabledSubsystems.INDEXER_ENABLED)
        {
            Indexer.getInstance().setDefaultCommand(new IndexingProcedureCommand());
            //System.out.println("The Indexer default command line has been passed");
        }
        if(EnabledSubsystems.TRANSFER_ENABLED)
        {
            CommandScheduler.getInstance().setDefaultCommand(Transfer.getInstance(), new TransferProcedureCommand());
            //System.out.println("The Transfer default command line has been passed");
        }

        if(EnabledSubsystems.TURRET_ENABLED)
        {
            //Turret.getInstance().setDefaultCommand(new SetTurretLimelightCommand());
        }

        if(EnabledSubsystems.CLIMB_DEPLOY_ENABLED){
            //ClimbDeploy.getInstance().setDefaultCommand(new ClimbDeployJoystickCommand());
        }
        if(EnabledSubsystems.TRANSFER_HOOK_ENABLED)
        {
            //TransferHook.getInstance().setDefaultCommand(new TransferHookJoystickCommand());
        }
        
        //System.out.println("end of robot init");
    }

    public void autoChooserInit() {
        AutoChoosers.startPosChooser.setDefaultOption("Target", startPos.directlyInFrontOfGoal);
        AutoChoosers.startPosChooser.addOption("Trench", startPos.directlyInFrontOfTrench);
        AutoChoosers.startPosChooser.addOption("Middle Of Nowhere", startPos.middleOfNowhere);
        AutoChoosers.startPosChooser.addOption("Two Pos", startPos.twoPos);
        AutoChoosers.startPosChooser.addOption("Three Pos", startPos.threePosition);

        AutoChoosers.ballLocationChooser.setDefaultOption("None", ballLocation.none);
        AutoChoosers.ballLocationChooser.addOption("Trench", ballLocation.trench);
        AutoChoosers.ballLocationChooser.addOption("Three Rendezvous", ballLocation.threeRendezvous);
        AutoChoosers.ballLocationChooser.addOption("Two Rendezvous", ballLocation.twoRendezvous);

        SmartDashboard.putData("Auto Start Pos", AutoChoosers.startPosChooser);
        SmartDashboard.putData("Auto Ball Loc", AutoChoosers.ballLocationChooser);
    }

    public void smartDashboardInit() {

        SmartDashboard.putData(new CSVSaverEnable());
        SmartDashboard.putData(new CSVSaverDisable());
        SmartDashboard.putNumber("Auto Wait Time", 0);

        //SmartDashboard.putData(new IndexingProcedureCommand());
        //SmartDashboard.putData(new TransferProcedureCommand());

        // SmartDashboard.putData(new SimpleMiddleAutoCommand());

        // SmartDashboard.putData(new ToggleDeployBashBarCommand());

        if (EnabledSubsystems.DRIVE_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putNumber("Right Current", Drive.getInstance().getRightCurrent());
            SmartDashboard.putNumber("Left Current", Drive.getInstance().getLeftCurrent());

            SmartDashboard.putData(new DriveForwardBasicSmartDashboardCommand());
            SmartDashboard.putData(new EnableMotionProfileSmartDashboardCommand());
            SmartDashboard.putData(new TurnSmartDashboardCommand());
            SmartDashboard.putData(new EnableTwoDMotionProfileSmartDashboardCommand());
            SmartDashboard.putData(new EnableReverseTwoDMotionProfileSmartDashboardCommand());

            SmartDashboard.putData(new DriveForwardBasicSmartDashboardCommand());
        }

        if (EnabledSubsystems.TURRET_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putData(new SetTurretAngleSmartDashboardCommand());
            SmartDashboard.putData(new DeltaTurretAngleSmartDashboardCommand());
            SmartDashboard.putData(new SetTurretLimelightCommand());
            SmartDashboard.putData(new SetTurretPercentCommand());
            SmartDashboard.putData(new ZeroTurretEncoderCommand());
        }

        if (EnabledSubsystems.SHOOTER_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putData(new SetShooterSpeedSmartDashboardCommand());
        }
        if (EnabledSubsystems.INDEXER_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putData(new ProjectileVomitCommand());
            SmartDashboard.putData(new IndexerDeltaPositionSmartDashboardCommand());
            SmartDashboard.putData(new IndexerSetVelocitySmartDashboardCommand());
            SmartDashboard.putData(new IndexingProcedureCommand());
            SmartDashboard.putData(new IndexerPukeCommand());
        }

        if (EnabledSubsystems.CLIMB_DEPLOY_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putData(new ClimbDeploySmartDashboardCommand());
        }

        if (EnabledSubsystems.WINCH_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putData(new SetWinchPositionCommand());
            SmartDashboard.putData(new WinchClimbRetractCommand());
        }

        if (EnabledSubsystems.TRANSFER_SMARTDASHBOARD_DEBUG_ENABLED) {
            // SmartDashboard.putData(new TransferCommand2(Transfer.TRANSFER_TIME));
            SmartDashboard.putData(new TransferProcedureCommand());
            // RIP TransferCommand2. You made your country proud
            SmartDashboard.putData(new TransferCommand(Transfer.TRANSFER_TIME));
        }

        if (EnabledSubsystems.COLOR_WHEEL_SMARTDASHBOARD_ENABLED) {
            SmartDashboard.putData(new ColorWheelRotateCommand());
            SmartDashboard.putData(new TargetColorCommand());
            SmartDashboard.putData(new ToggleColorWheelSmartDashboardCommand());
        }

        if(EnabledSubsystems.HOOD_ENABLED)
        {
            SmartDashboard.putData(new HoodPercentCommand());
            SmartDashboard.putData(new SetHoodAngleSmartDashboardCommand());
            SmartDashboard.putData(new DeltaHoodAngleSmartDashboardCommand());
        }

        //SmartDashboard.putData("Turn to Angle", new TurnSmartDashboardCommand());

        // SmartDashboard.putNumber("F TESTER", 0);
        // SmartDashboard.putNumber("P TESTER", 0);
        // SmartDashboard.putNumber("MOTOR PERCENT", 0);
        // SmartDashboard.putNumber("TESTER SENSOR POSITION", 0);
    }

    @Override
    public void disabledInit() {
        for (NRSubsystem subsystem : NRSubsystem.subsystems) {
            subsystem.disable();
        }
    }

    @Override
    public void testInit() {
        enabledInit();
    }

    public void disabledPeriodic() {

    }

    public void autonomousInit() {
        enabledInit();
        selectedBallLocation = AutoChoosers.ballLocationChooser.getSelected();
        selectedStartPos = AutoChoosers.startPosChooser.getSelected();

        autonomousCommand = getAutoCommand();

        if (autonomousCommand != null)
            autonomousCommand.schedule();
    }

    public void autonomousPeriodic() {

    }

    public void teleopInit() {
        enabledInit();
        // new CancelAllCommand().start(); maybe? depending on gameplay

        // LimelightNetworkTable.getInstance().lightLED(true);
        // LimelightNetworkTable.getInstance().lightLED(false);
        /*SmartDashboard.putNumber("Hood goal angle", 0);
        SmartDashboard.putNumber("testSpark Set Angle", 0);

        SmartDashboard.putNumber("testSpart F", 0);
        SmartDashboard.putNumber("testSpart P", 0);
        SmartDashboard.putNumber("testSpart I", 0);
        SmartDashboard.putNumber("testSpart D", 0);

        SmartDashboard.putNumber("testSpark Percent", 0);*/
    }

    public void teleopPeriodic() {

        dt = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - prevTime;
        prevTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
        dtTot += dt;
        count++;

        if (count % 100 == 0) {
            // System.out.println(dtTot / 100);
            dtTot = 0;
            count = 0;
        }

        //testSpark.set(SmartDashboard.getNumber("testSpark Percent", 0));

        // tester.config_kF(0, SmartDashboard.getNumber("F TESTER", 0));
        // tester.config_kP(0, SmartDashboard.getNumber("P TESTER", 0));

        // tester.set(ControlMode.PercentOutput, SmartDashboard.getNumber("MOTOR
        // PERCENT", 0));

        // SmartDashboard.putNumber("TESTER SENSOR POSITION",
        // tester.getSensorCollection().getIntegratedSensorPosition());
    }

    /*
     * public void CameraInit() { new Thread(() -> { UsbCamera camera =
     * CameraServer.getInstance().startAutomaticCapture(); camera.setResolution(720,
     * 1080);
     * 
     * }).start();
     * 
     * }
     */

    @Override
    public void testPeriodic() {
    }

    /*public void setAngle(Angle target) {
        if (testSpark != null) {
            // System.out.println(setAngleHood.get(Angle.Unit.DEGREE) *
            // Hood.HoodDegreePerMotorRotation);
            if (target.get(Angle.Unit.DEGREE) < 24) {
                target = new Angle(24, Angle.Unit.DEGREE);
            }
            SmartDashboard.putNumber("testSpark Set Angle", target.get(Angle.Unit.DEGREE));
            Angle a = new Angle(target.get(Angle.Unit.DEGREE) - 24, Angle.Unit.DEGREE);
            double b = a.get(Angle.Unit.DEGREE) * 1.815;
            //testSpark.getPIDController().setReference(b, ControlType.kPosition, 1);
            // hoodSpark.getPIDController().seReference(setAngleHood.get(Angle.Unit.ROTATION),
            // ControlType.kPosition, POS_SLOT);
        }
    }*/

    @Override
    public void robotPeriodic() {

        CommandScheduler.getInstance().run();
        Periodic.runAll();
        SmartDashboardSource.runAll();

    }

    public void enabledInit() {

    }

    public Command getAutoCommand() {
        if (selectedStartPos == startPos.middleOfNowhere)
            // It's a five for five
            return new MiddleOfNowhereCommand();
        else if(selectedStartPos == startPos.directlyInFrontOfGoal)
            return new JustShootCommand();
        else if (selectedStartPos == startPos.threePosition && selectedBallLocation == ballLocation.none)
            return new SimpleMiddleAutoCommand();
        else if (selectedStartPos == startPos.threePosition && selectedBallLocation == ballLocation.threeRendezvous)
            return new MiddleToThreeRendezvousAutoCommand();
        else if(selectedStartPos == startPos.twoPos && selectedBallLocation == ballLocation.none)
            return new SimpleMiddleAutoCommand();
        else if (selectedStartPos == startPos.twoPos && selectedBallLocation == ballLocation.twoRendezvous)
            return new MiddleToTwoRendezvousCommand();
        else if (selectedStartPos == startPos.directlyInFrontOfTrench)
            return new DirectlyInFrontOfTrenchCommand();
        return new DoNothingCommand();
    }

    public double getPeriod() {
        return period;
    }
}