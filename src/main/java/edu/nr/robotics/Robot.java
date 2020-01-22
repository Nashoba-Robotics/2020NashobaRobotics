package edu.nr.robotics;

import com.revrobotics.CANSparkMax;
<<<<<<< HEAD
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.nr.lib.motorcontrollers.SparkMax;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;



=======
import edu.nr.lib.motorcontrollers.SparkMax;

>>>>>>> cb05abe47ec261cb467b3edaefb2503188699e6a
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
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.drive.CSVSaverDisable;
import edu.nr.robotics.subsystems.drive.CSVSaverEnable;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveForwardBasicSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.EnableMotionProfileSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.EnableReverseTwoDMotionProfileSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.EnableTwoDMotionProfileSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.TurnSmartDashboardCommand;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.sensors.ISquaredCSensor;
import edu.nr.robotics.subsystems.shooter.SetShooterSpeedSmartDashboardCommand;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.turret.DeltaTurretAngleSmartDashboardCommand;
import edu.nr.robotics.subsystems.turret.SetTurretAngleSmartDashboardCommand;
import edu.nr.robotics.subsystems.turret.Turret;

/*import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;*/
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

    private static Robot singleton;

    private static double period = 0.015;

    private CANSparkMax protoShooter1;
    private CANSparkMax protoShooter2;

    public static double F_VEL_SHOOTER = 0;
    public static double P_VEL_SHOOTER = 0;
    public static double I_VEL_SHOOTER = 0;
    public static double D_VEL_SHOOTER = 0;

    public static AngularSpeed shooterSetSpeed;

    double dt;
    double dtTot = 0;
    int count = 0;

    private double prevTime = 0;

    private CANSparkMax protoSparkMax1;
    private CANSparkMax protoSparkMax2;

    private Command autonomousCommand;
   
    public double autoWaitTime;
    //public Compressor robotCompressor;

    public synchronized static Robot getInstance() {
        return singleton;
    }

    public void robotInit() {
        singleton = this;

        m_period = period; // period that the code runs at

        smartDashboardInit();
        autoChooserInit();
        OI.init();
        Drive.getInstance();
        Turret.getInstance();
        //Shooter.getInstance();
        //Hood.getInstance();
       
        //robotCompressor = new Compressor(RobotMap.PCM_ID);
        //robotCompressor.start();

        // CameraInit();

        LimelightNetworkTable.getInstance().lightLED(true);
        LimelightNetworkTable.getInstance().setPipeline(Pipeline.Target);
        //System.out.println("end of robot init");
<<<<<<< HEAD

        protoShooter1 = SparkMax.createSpark(1, true);
        protoShooter2 = SparkMax.createSpark(11, true);

        protoShooter1.getPIDController().setFF(F_VEL_SHOOTER, 0);
        protoShooter1.getPIDController().setP(P_VEL_SHOOTER, 0);
        protoShooter1.getPIDController().setI(I_VEL_SHOOTER, 0);
        protoShooter1.getPIDController().setD(D_VEL_SHOOTER, 0);

        protoShooter2.getPIDController().setFF(F_VEL_SHOOTER, 0);
        protoShooter2.getPIDController().setP(P_VEL_SHOOTER, 0);
        protoShooter2.getPIDController().setI(I_VEL_SHOOTER, 0);
        protoShooter2.getPIDController().setD(D_VEL_SHOOTER, 0);

            //protoShooter1.setSmartCurrentLimit(40);
            //protoShooter1.setSecondaryCurrentLimit(60);

            protoShooter1.enableVoltageCompensation(12);

            protoShooter1.setClosedLoopRampRate(0.05);
            protoShooter1.setOpenLoopRampRate(0.05);

            protoShooter1.getPIDController().setOutputRange(-1, 1, 0);

            //protoShooter2.setSmartCurrentLimit(40);
            //protoShooter2.setSecondaryCurrentLimit(60);

            protoShooter2.enableVoltageCompensation(12);

            protoShooter2.setClosedLoopRampRate(0.05);
            protoShooter2.setOpenLoopRampRate(0.05);

            protoShooter2.getPIDController().setOutputRange(-1, 1, 0);

            protoShooter1.setCANTimeout(10);
            protoShooter2.setCANTimeout(10);
=======
        protoSparkMax1 = SparkMax.createSpark(0, true);
        protoSparkMax2 = SparkMax.createSpark(0, true);
>>>>>>> cb05abe47ec261cb467b3edaefb2503188699e6a
    }

    public void autoChooserInit() {
        

      

    }

    public void smartDashboardInit() {

        SmartDashboard.putData(new CSVSaverEnable());
        SmartDashboard.putData(new CSVSaverDisable());
        SmartDashboard.putNumber("Auto Wait Time", 0);

        if (EnabledSubsystems.DRIVE_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putData(new DriveForwardBasicSmartDashboardCommand());
            SmartDashboard.putData(new EnableMotionProfileSmartDashboardCommand());
			SmartDashboard.putData(new TurnSmartDashboardCommand());
            SmartDashboard.putData(new EnableTwoDMotionProfileSmartDashboardCommand());
            SmartDashboard.putData(new EnableReverseTwoDMotionProfileSmartDashboardCommand());
        }

        if(EnabledSubsystems.TURRET_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putData(new SetTurretAngleSmartDashboardCommand());
            SmartDashboard.putData(new DeltaTurretAngleSmartDashboardCommand());
        }

        if(EnabledSubsystems.SHOOTER_SMARTDASHBOARD_DEBUG_ENABLED){
            //SmartDashboard.putData(new SetShooterSpeedSmartDashboardCommand());
            SmartDashboard.putNumber("Prototype Speed Percent: ", 0);
            SmartDashboard.putNumber("A Prototype Current Reading: ", 0);
            SmartDashboard.putNumber("B Prototype Current Reading: ", 0);
        }

        SmartDashboard.putNumber("TEST SHOOTER SPEED", 0);

        SmartDashboard.putNumber("Shooter1 Current: ", 0);
        SmartDashboard.putNumber("Shooter2 Current: ", 0);

        SmartDashboard.putNumber("Shooter1 Speed: ", 0);
        SmartDashboard.putNumber("Shooter2 Speed: ", 0);

        SmartDashboard.putNumber("F Vel Shooter: ", F_VEL_SHOOTER);
		SmartDashboard.putNumber("P Vel Shooter: ", P_VEL_SHOOTER);
		SmartDashboard.putNumber("I Vel Shooter: ", I_VEL_SHOOTER);
        SmartDashboard.putNumber("D Vel Shooter: ", D_VEL_SHOOTER);
    }

        @Override
        public void disabledInit() {
            for(NRSubsystem subsystem : NRSubsystem.subsystems) {
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

            

        }

        public void autonomousPeriodic() {

        }

        public void teleopInit() {
            enabledInit();
            //new CancelAllCommand().start(); maybe? depending on gameplay

           // LimelightNetworkTable.getInstance().lightLED(true);
           // LimelightNetworkTable.getInstance().lightLED(false);
        }

        public void teleopPeriodic() {

            dt = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - prevTime;
            prevTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
            dtTot += dt;
            count++;

            if (count % 100 == 0) {
                //System.out.println(dtTot / 100);
                dtTot = 0;
                count = 0;
            }
<<<<<<< HEAD
            if(protoShooter2 != null)
            {

            shooterSetSpeed = new AngularSpeed(SmartDashboard.getNumber("TEST SHOOTER SPEED", 0), Angle.Unit.ROTATION, Time.Unit.MINUTE);
           
            //protoShooter1.set(SmartDashboard.getNumber("Prototype Speed Percent: ", 0));
            //protoShooter2.set(-1 * SmartDashboard.getNumber("Prototype Speed Percent: ", 0));

            protoShooter1.getPIDController().setReference(shooterSetSpeed.get(Angle.Unit.ROTATION, Time.Unit.MINUTE), ControlType.kVelocity, 0);
            protoShooter2.getPIDController().setReference(-1 * shooterSetSpeed.get(Angle.Unit.ROTATION, Time.Unit.MINUTE), ControlType.kVelocity, 0);

            //protoShooter1.getPIDController().setReference(180, ControlType.kVelocity, 0);
            //protoShooter2.getPIDController().setReference(-180, ControlType.kVelocity, 0);


            SmartDashboard.putNumber("Shooter1 Current: ", protoShooter1.getOutputCurrent());
            SmartDashboard.putNumber("Shooter2 Current: ", protoShooter2.getOutputCurrent());

            F_VEL_SHOOTER = SmartDashboard.getNumber("F Vel Shooter: ", F_VEL_SHOOTER);
            protoShooter1.getPIDController().setFF(F_VEL_SHOOTER, 0);
            protoShooter2.getPIDController().setFF(F_VEL_SHOOTER, 0);
           
            P_VEL_SHOOTER = SmartDashboard.getNumber("P Vel Shooter: ", P_VEL_SHOOTER);
            protoShooter1.getPIDController().setP(P_VEL_SHOOTER, 0);
            protoShooter2.getPIDController().setP(P_VEL_SHOOTER, 0);
           
            I_VEL_SHOOTER = SmartDashboard.getNumber("I Vel Shooter: ", I_VEL_SHOOTER);
            protoShooter1.getPIDController().setI(I_VEL_SHOOTER, 0);
            protoShooter2.getPIDController().setI(I_VEL_SHOOTER, 0);
           
            D_VEL_SHOOTER = SmartDashboard.getNumber("D Vel Shooter: ", D_VEL_SHOOTER);
            protoShooter1.getPIDController().setD(D_VEL_SHOOTER, 0);
            protoShooter2.getPIDController().setD(D_VEL_SHOOTER, 0);

            SmartDashboard.putNumber("Shooter1 Speed: ", protoShooter1.getEncoder().getVelocity());
            SmartDashboard.putNumber("Shooter2 Speed: ", protoShooter2.getEncoder().getVelocity());
            }
=======
            protoSparkMax1.set(SmartDashboard.getNumber("Prototype Speed Percent: ", 0));
            protoSparkMax2.set(SmartDashboard.getNumber("Prototype Speed Percent: ", 0));

            SmartDashboard.putNumber("A Prototype Current Reading: ", protoSparkMax1.getOutputCurrent());
            SmartDashboard.putNumber("B Prototype Current Reading: ", protoSparkMax2.getOutputCurrent());


>>>>>>> cb05abe47ec261cb467b3edaefb2503188699e6a
        }

       /* public void CameraInit() {
            new Thread(() -> {
                UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
                camera.setResolution(720, 1080);
                
            }).start();
            
        }*/

        @Override
        public void testPeriodic() {

        } 
        @Override
        public void robotPeriodic() {

            CommandScheduler.getInstance().run();
            Periodic.runAll();
            SmartDashboardSource.runAll();


        }

        public void enabledInit() {
            //new LiftLockMechanismRetractCommand().start();
        }
    
        public Command getAutoCommand() {
           return  new DoNothingCommand();
        }

        public double getPeriod() {
            return period;
        }

    }

