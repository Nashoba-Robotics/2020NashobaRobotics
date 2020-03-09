package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;

public class HoodPercentCommand extends NRCommand
{
    public HoodPercentCommand()
    {
        super(Hood.getInstance());
    }

    @Override
    protected void onStart()
    {
        //Hood.getInstance().setMotorSpeedRaw(Hood.goalPercent);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}