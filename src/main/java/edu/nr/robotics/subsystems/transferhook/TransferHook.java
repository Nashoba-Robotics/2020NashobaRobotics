package edu.nr.robotics.subsystems.transferhook;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TransferHook extends NRSubsystem
{
    private static TransferHook singleton;

    private CANSparkMax transferHookSpark;

    public static double goalPercent;

    private TransferHook()
    {
        if(EnabledSubsystems.TRANSFER_HOOK_ENABLED)
        {
            transferHookSpark = new CANSparkMax(RobotMap.TRANSFER_HOOK_SPARK, MotorType.kBrushless);
            
        }
        SmartDashboardInit();
    }

    public static void init()
    {
        if(singleton == null)
            singleton = new TransferHook();
    }

    public static TransferHook getInstance()
    {
        if(singleton == null)
            init();
        return singleton;
    }

    public void SmartDashboardInit()
    {
        SmartDashboard.putNumber("Encoder Ticks TransferHook", getEncoderTicks());
        SmartDashboard.putNumber("TransferHook Motor Speed", getMotorSpeed().get(Angle.Unit.ROTATION, Time.Unit.MINUTE));
        SmartDashboard.putNumber("Set TransferHook Motor Percent", 0);
    }

    @Override
    public void smartDashboardInfo() 
    {
        SmartDashboard.putNumber("Encoder Ticks TransferHook", getEncoderTicks());
        SmartDashboard.putNumber("TransferHook Motor Speed", getMotorSpeed().get(Angle.Unit.ROTATION, Time.Unit.MINUTE));
        goalPercent = SmartDashboard.getNumber("Set TransferHook Motor Percent", 0);
    }

    public void setMotorSpeedRaw(double percent)
    {
        if(transferHookSpark != null)
        {
            transferHookSpark.set(percent);
        }
    }

    public AngularSpeed getMotorSpeed()
    {
        if(transferHookSpark != null)
        {
            return new AngularSpeed(transferHookSpark.getEncoder().getVelocity(), Angle.Unit.ROTATION, Time.Unit.MINUTE);
        }
        return AngularSpeed.ZERO;
    }

    public double getEncoderTicks()
    {
        if(transferHookSpark != null)
            return transferHookSpark.getEncoder().getPosition();
        return 0;
    }

    @Override
    public void disable() 
    {

    }

    
}