package edu.nr.robotics;

import edu.nr.lib.commandbased.DoNothingCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.drive.CSVSaverDisable;
import edu.nr.robotics.subsystems.drive.CSVSaverEnable;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveForwardBasicSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.DriveForwardSmartDashboardCommandH;
import edu.nr.robotics.subsystems.drive.EnableMotionProfileSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.EnableReverseTwoDMotionProfileSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.EnableTwoDMotionProfileSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.TurnSmartDashboardCommand;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

    private static Robot singleton;

    private static double period = 0.02;

    double dt;
    double dtTot = 0;
    int count = 0;

    private double prevTime = 0;

    private Command autonomousCommand;
   
    public double autoWaitTime;
    public Compressor robotCompressor;

    public synchronized static Robot getInstance() {
        return singleton;
    }

    public void robotInit() {
        singleton = this;

        m_period = period; // period that code runs at

        smartDashboardInit();
        autoChooserInit();
        OI.init();
        Drive.getInstance();
       
        robotCompressor = new Compressor(RobotMap.PCM_ID);
        robotCompressor.start();

        // CameraInit();

        LimelightNetworkTable.getInstance().lightLED(false);
        //System.out.println("end of robot init");

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
			SmartDashboard.putData(new DriveForwardSmartDashboardCommandH());
			SmartDashboard.putData(new TurnSmartDashboardCommand());
            SmartDashboard.putData(new EnableTwoDMotionProfileSmartDashboardCommand());
            SmartDashboard.putData(new EnableReverseTwoDMotionProfileSmartDashboardCommand());
        }

        

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
        
        }

        public void CameraInit() {
            new Thread(() -> {
                UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
                camera.setResolution(720, 1080);
                
            }).start();
            
        }
        @Override
        public void testPeriodic() {

        } 
        @Override
        public void robotPeriodic() {

            Scheduler.getInstance().run();
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

