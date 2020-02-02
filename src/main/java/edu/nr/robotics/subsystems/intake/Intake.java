package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRSubsystem;

import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Time.Unit;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularAcceleration;
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

    public static final AngularSpeed MAX_SPEED_INTAKE = new AngularSpeed(1, Angle.Unit.DEGREE, Time.Unit.SECOND);
    public static final AngularAcceleration MAX_ACCELERATION_INTAKE = new AngularAcceleration(1, Angle.Unit.DEGREE, Time.Unit.SECOND, Time.Unit.SECOND);

    public static AngularSpeed currentAngularSpeed = AngularSpeed.ZERO;

    private Intake()
    {
        super();
        if(EnabledSubsystems.INTAKE_ENABLED)
        {
            IntakeTalon = CTRECreator.createMasterTalon(0);
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
        if (IntakeSparkMax != null)
            IntakeSparkMax.set(percent);
        if (IntakeTalon != null)
            IntakeTalon.set(ControlMode.PercentOutput, percent);
    }
    
    /*
    public void setMotorSpeedPercent(double percent) {
        if (IntakeSparkMax != null)
            if (EnabledSubsystems.INTAKE_ENABLED)
                setMotorSpeedRaw(percent);
            else
               // setMotorSpeed(MAX_SPEED_INTAKE.mul(percent));
               setMotorSpeedPercent(percent);
    }
    */



    public AngularSpeed getIdealMotorSpeed()
    {
        return currentAngularSpeed;
    }

    //Hopefully returns actual motor speed
    public double getActualMotorSpeed()
    {
        if (IntakeSparkMax != null){
            return IntakeSparkMax.get();
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