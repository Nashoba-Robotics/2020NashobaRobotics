package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.wpi.first.wpilibj.Timer;

public class DrivePercentSmartDashboardCommand extends NRCommand
{
    private double startTime;

    private double start;

    private double end;

    public DrivePercentSmartDashboardCommand()
    {
        super(Drive.getInstance());
    }

    @Override
    protected void onStart()
    {
        startTime = Timer.getFPGATimestamp();
        start = Timer.getFPGATimestamp();
    }

    @Override
    protected void onExecute()
    {
        end = Timer.getFPGATimestamp();   
        if(end - startTime < 1)
            Drive.getInstance().setMotorSpeedInPercent(Drive.goalPercent * (end - startTime), Drive.goalPercent * (end - startTime));
        else
            Drive.getInstance().setMotorSpeedInPercent(Drive.goalPercent, Drive.goalPercent);
    }

    @Override
    protected void onEnd()
    {
        Drive.rampDown = true;
        Drive.startTime = Timer.getFPGATimestamp() + 1;
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}