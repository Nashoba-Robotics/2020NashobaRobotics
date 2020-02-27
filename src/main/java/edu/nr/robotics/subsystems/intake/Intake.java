package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRSubsystem;

import edu.nr.lib.units.Time;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
//import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Intake extends NRSubsystem
{
    private static Intake singleton;

    private VictorSPX IntakeVictor;

    private Solenoid IntakeSolenoid;

    public static final Time ACTUATION_TIME = new Time(0.5, Time.Unit.SECOND);

    // Change Percent values
    public static final double MAX_PERCENT_INTAKE = 1.0;
    public static final double MIN_PERCENT_INTAKE = -1.0;

    public static double currentIntakePercent = 0.0;

    public static final double INTAKE_PERCENT = 0.8;

    public static final double PUKE_PERCENT = -1;

    private Intake()
    {
        super();
        if(EnabledSubsystems.INTAKE_ENABLED)
        {
            IntakeVictor= new VictorSPX(RobotMap.INTAKE_VICTOR);
        }
        SmartDashboardInit();
    }

    public synchronized static void init()
    {
        if(singleton == null)
        {
            singleton = new Intake();
        }
    }

    public static Intake getInstance()
    {
        if(singleton == null)
        {
            init();
        }
        return singleton;
    }

    public void disable() 
    {
        if(IntakeSolenoid != null)
            retractIntake();
        if(IntakeVictor != null)
            setMotorSpeedRaw(0);
    }

    public enum State {
		DEPLOYED, RETRACTED;
		
		private static boolean DEPLOYED_VALUE = true;
		private static boolean RETRACTED_VALUE = false;
		
		private static State getDeployState(boolean val) {
			if(val == State.DEPLOYED_VALUE) {
				return State.DEPLOYED;
			} else {
				return State.RETRACTED;
			}
		}
    }
    
    public State currentDeployState() {
		if(IntakeSolenoid != null) {
			return State.getDeployState(IntakeSolenoid.get());
		} else {
			return State.RETRACTED;
		}
    }

    public boolean isIntakeDeployed()
    {
       return currentDeployState() == State.DEPLOYED;
    }

    public void deployIntake() {
		if (IntakeSolenoid != null) {
			IntakeSolenoid.set(State.DEPLOYED_VALUE);
		}
	}

	public void retractIntake() {
		if (IntakeSolenoid != null) {
			IntakeSolenoid.set(State.RETRACTED_VALUE);
		}
    }

    public double getOutputCurrent()
    {
        if(IntakeVictor != null)
        {
            //return IntakeVictor.getStatorCurrent();
        }
        return 0;
    }

    public void setMotorSpeedRaw(double percent) {
        if (IntakeVictor != null){
            if(percent > MAX_PERCENT_INTAKE)
                percent = MAX_PERCENT_INTAKE;
            else if(percent < MIN_PERCENT_INTAKE)
                percent = MIN_PERCENT_INTAKE;
            IntakeVictor.set(ControlMode.PercentOutput, percent);
            currentIntakePercent = percent;
        }
    }

    public double getIdealMotorSpeed()
    {
        return currentIntakePercent;
    }

    public double getActualMotorSpeed()
    {
        if(IntakeVictor != null)
        {
            return IntakeVictor.getSelectedSensorVelocity();
        }
        return 0;
    }

    public void SmartDashboardInit() {
		if (EnabledSubsystems.INTAKE_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putBoolean("Intake deployed: ", isIntakeDeployed());
            SmartDashboard.putNumber("Intake Ideal Motor Speed: ", getIdealMotorSpeed());
            SmartDashboard.putNumber("Intake Actual Motor Speed: ", getActualMotorSpeed());
            SmartDashboard.putNumber("SET SPEED INTAKE (PERCENT)", 0);
		}
	}

	@Override
	public void smartDashboardInfo() {
        if(EnabledSubsystems.INTAKE_SMARTDASHBOARD_DEBUG_ENABLED)
        {
            SmartDashboard.putBoolean("Intake Deployed: ", isIntakeDeployed());
            setMotorSpeedRaw(SmartDashboard.getNumber("SET SPEED INTAKE (PERCENT)", 0));
            SmartDashboard.putNumber("Intake Ideal Motor Speed: ", getIdealMotorSpeed());
            SmartDashboard.putNumber("Intake Actual Motor Speed: ", getActualMotorSpeed());
        }
	}

	@Override
	public void periodic() {

    }
}