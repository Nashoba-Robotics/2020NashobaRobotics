package edu.nr.robotics.subsystems.turret;
 
import java.security.Provider;
 
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.motorcontrollers.SparkMax;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularAcceleration;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.DigitalSensor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
 
public class Turret extends NRSubsystem
{
    private static Turret singleton;
    private CANSparkMax turretSpark;
 
    private CANPIDController turretPIDController;
    private CANEncoder turretEncoder;
 
    //Pleas check if ENCODER_TICKS_DEGREE_SPARK is correct
    public static final double ENCODER_TICKS_PER_DEGREE_SPARK = (42.0 / 360);
 
    private static double F_POS_TURRET = 0;
    private static double P_POS_TURRET = 0.1;
    private static double I_POS_TURRET = 1.5e-4;
    private static double D_POS_TURRET = 1;
 
    public static double turretMinOutput = -1;
    private static double turretMaxOutput = 1;
 
    private static Angle setAngle = Angle.ZERO;
    public static Angle goalAngle = Angle.ZERO;
    public static Angle deltaAngle = Angle.ZERO;
 
    public static final IdleMode IDLE_MODE_TURRET = IdleMode.kBrake;
    //Type of PID. 0 = primary. 1 = cascade
    public static final int PID_TYPE = 0;
 
    public static final int POS_SLOT = 1;
 
    private Turret()
    {
        if(EnabledSubsystems.TURRET_ENABLED) {
            turretSpark = SparkMax.createSpark(RobotMap.TURRET_SPARK, true);
 
            turretSpark.restoreFactoryDefaults();
 
            turretPIDController = turretSpark.getPIDController();
 
            turretEncoder = turretSpark.getEncoder();
 
            turretPIDController.setP(P_POS_TURRET);
            turretPIDController.setI(I_POS_TURRET);
            turretPIDController.setD(D_POS_TURRET);
            turretPIDController.setFF(F_POS_TURRET);
            turretPIDController.setOutputRange(turretMinOutput, turretMaxOutput);
 
            turretSpark.setIdleMode(IDLE_MODE_TURRET);
 
            if(EnabledSubsystems.TURRET_DUMB_ENABLED) {
                turretSpark.set(0);
            }
            else{
                turretSpark.set(0);
            }
            smartDashboardInit();
        }
    }
 
    public synchronized static void init()
    {
        if(singleton == null)
            singleton = new Turret();
    }
 
    public static Turret getInstance()
    {
        if(singleton == null)
            init();
        return singleton;
    }
 
    public void disable()
    {
        if(turretSpark != null)
        {
            turretSpark.stopMotor();
        }
    }
 
    public void smartDashboardInit()
    {
        if(EnabledSubsystems.TURRET_SMARTDASHBOARD_DEBUG_ENABLED) 
        {
            SmartDashboard.putNumber("P_POS_TURRET", 0);
            SmartDashboard.putNumber("I_POS_TURRET", 0);
            SmartDashboard.putNumber("D_POS_TURRET", 0);
            SmartDashboard.putNumber("F_POS_TURRET", 0);
            SmartDashboard.putNumber("Max Output", turretMaxOutput);
            SmartDashboard.putNumber("Min Output", turretMinOutput);
            SmartDashboard.putNumber("Set Angle (Rotations)", 0);
            SmartDashboard.putNumber("Current Angle (Rotations)", getAngle().get(Angle.Unit.ROTATION));
        }
    }
 
    public void smartDashboardInfo()
    {
        if(turretSpark != null)
        {
            if(EnabledSubsystems.TURRET_SMARTDASHBOARD_DEBUG_ENABLED)
            {
                P_POS_TURRET = SmartDashboard.getNumber("P_POS_TURRET", 0);
                I_POS_TURRET = SmartDashboard.getNumber("I_POS_TURRET", 0);
                D_POS_TURRET = SmartDashboard.getNumber("D_POS_TURRET", 0);
                F_POS_TURRET = SmartDashboard.getNumber("F_POS_TURRET", 0);
 
                turretMaxOutput = SmartDashboard.getNumber("Max Output", 0);
                turretMinOutput = SmartDashboard.getNumber("Min Output", 0);
 
                turretPIDController.setP(P_POS_TURRET);
                turretPIDController.setI(I_POS_TURRET);
                turretPIDController.setD(D_POS_TURRET);
                turretPIDController.setFF(F_POS_TURRET);
 
                goalAngle = new Angle(SmartDashboard.getNumber("Target Rotations", 0), Angle.Unit.ROTATION);
                SmartDashboard.putNumber("Set Angle (Rotations)", setAngle.get(Angle.Unit.ROTATION));
                SmartDashboard.putNumber("Current Angle (Rotations)", getAngle().get(Angle.Unit.ROTATION));
            }
        }
        //System.out.println("Turret angle" + goalAngle.get(Angle.Unit.ROTATION));
        turretPIDController.setReference(5, ControlType.kPosition);
    }
 
    public Angle getAngle()
    {
        if(turretSpark != null)
        {
            return new Angle(turretEncoder.getPosition(), Angle.Unit.ROTATION);
        }
        return Angle.ZERO;
    }
 
    public AngularSpeed getActualSpeed(){
        if(turretSpark != null)
        {
            return new AngularSpeed(turretEncoder.getVelocity(), Angle.Unit.ROTATION, Time.Unit.MINUTE);
        }

        return AngularSpeed.ZERO;
    }

    public AngularSpeed getSetSpeed()
    {
        if(turretSpark != null)
        {
            
        }
        return AngularSpeed.ZERO;
    }
 
    public void setAngle(Angle targetAngle){
 
        if(turretSpark != null){
            setAngle = targetAngle;
            turretPIDController.setReference(targetAngle.get(Angle.Unit.ROTATION), ControlType.kPosition);
        }
    }
 
    public void periodic()
    {
        if(turretSpark != null)
        {
 
        }
    }
 
    public double getCurrent(){
        if(turretSpark != null)
        {
            return turretSpark.getOutputCurrent();
        }
        return 0;
    }

    public void setMotorSpeedInPercent(double percent)
    {
        if(turretSpark != null)
        {
            turretSpark.set(percent);
        }
    }
}
