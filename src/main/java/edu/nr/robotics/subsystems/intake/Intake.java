package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRSubsystem;

import edu.nr.lib.units.Time;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.nr.lib.motorcontrollers.CTRECreator;
import edu.nr.lib.motorcontrollers.SparkMax;

public class Intake extends NRSubsystem
{
    private static Intake singleton;

    private CANSparkMax IntakeSparkMax;
    private TalonSRX IntakeTalon;

    private Solenoid IntakeSolenoid;

    public static final Time ACTUATION_TIME = new Time(0.5, Time.Unit.SECOND);

    // Change Percent values
    public static final double MAX_PERCENT_INTAKE = 1.0;
    public static final double MIN_PERCENTINTAKE = -1.0;

    public static double currentIntakePercent = 0.0;

    public static final double INTAKE_PERCENT = 0;

    private Intake()
    {
        super();
        if(EnabledSubsystems.INTAKE_ENABLED)
        {
            IntakeTalon = CTRECreator.createMasterTalon(RobotMap.INTAKE_SPARKMAX);
        }
        SmartDashboardInit();
    }

    public static void init()
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

    public double getCurrent()
    {
        if(IntakeSparkMax != null)
            return IntakeSparkMax.getOutputCurrent();
        return 0;
    }

    public void setMotorSpeedRaw(double percent) {
        if (IntakeTalon != null)
            IntakeTalon.set(ControlMode.PercentOutput, percent);
            currentIntakePercent = percent;
    }

    public double getIdealMotorSpeed()
    {
        return currentIntakePercent;
    }

    public double getActualMotorSpeed()
    {
        if (IntakeSparkMax != null){
            return IntakeSparkMax.getEncoder().getVelocity();
        }
        return 0;
    }

    public void SmartDashboardInit() {
		if (EnabledSubsystems.INTAKE_SMARTDASHBOARD_DEBUG_ENABLED) {
            SmartDashboard.putBoolean("Intake deployed: ", isIntakeDeployed());
            SmartDashboard.putNumber("Intake Motor Speed: ", getActualMotorSpeed());
            SmartDashboard.putNumber("SET SPEED INTAKE", 0);

		}
	}

	@Override
	public void smartDashboardInfo() {
        if(EnabledSubsystems.INTAKE_SMARTDASHBOARD_DEBUG_ENABLED)
        {
            SmartDashboard.putBoolean("Intake Deployed: ", isIntakeDeployed());
            setMotorSpeedRaw(SmartDashboard.getNumber("SET SPEED INTAKE", 0));

        }
	}

	@Override
	public void periodic() {

    }
}