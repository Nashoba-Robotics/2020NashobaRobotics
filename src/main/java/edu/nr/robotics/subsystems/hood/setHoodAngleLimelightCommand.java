package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;

public class setHoodAngleLimelightCommand extends NRCommand
{
    public setHoodAngleLimelightCommand()
    {
        super(Hood.getInstance());
    }

    @Override
    protected void onExecute()
    {
        Hood.getInstance().setAngle(Hood.getInstance().getAngleLimelight());
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}