package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRSubsystem;

import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Time.Unit;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularAcceleration;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.nr.lib.motorcontrollers.SparkMax;

public class Intake extends NRSubsystem
{
    private static Intake Singleton;

    private CANSparkMax IntakeSparkMax;

    private Solenoid IntakeSolenoid;

    public static final Time ACTUATION_TIME = new Time(0.5, Time.Unit.SECOND);

    public static final AngularSpeed MAX_ANGULAR_SPEED = new AngularSpeed(1, Angle.Unit.DEGREE, Time.Unit.SECOND);
    public static final AngularAcceleration MAX_ANGULAR_ACCELERATION = new AngularAcceleration(1, Angle.Unit.DEGREE, Time.Unit.SECOND, Time.Unit.SECOND);



    private Intake()
    {
        if(EnabledSubsystems.INTAKE_ENABLED)
        {
            IntakeSparkMax = SparkMax.createSpark(RobotMap.INTAKE_SPARKMAX, true);
        }
    }

    private static void init()
    {
        if(Singleton == null)
        {
            Singleton = new Intake();
        }
    }

    private static Intake getInstance()
    {
        if(Singleton == null)
        {
            init();
        }
        return Singleton;
    }

    public void disable() 
    {

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
			return State.DEPLOYED;
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



    public void SmartDashboardInit() {
		if (EnabledSubsystems.INTAKE_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putBoolean("INTAKE DEPLOYED: ", isIntakeDeployed());
		}
	}

	@Override
	public void smartDashboardInfo() {
		//SmartDashboard.putBoolean("Has Hatch", hasHatch());
        /*
		if(EnabledSubsystems.HATCH_MECHANISM_SMARTDASHBOARD_BASIC_ENABLED) {
            SmartDashboard.putString("Hatch Deploy Mechanism Position: ", currentDeployState().toString());
            SmartDashboard.putString("Hatch Mechanism State: ", currentHatchState().toString());
		}
		if(EnabledSubsystems.INTAKE_SMARTDASHBOARD_DEBUG_ENABLED) {
			hatchSensorThreshold = (int) SmartDashboard.getNumber("Hatch Sensor Threshold: ", hatchSensorThreshold);
        }
        */
	}

	@Override
	public void periodic() {

    }
    

}