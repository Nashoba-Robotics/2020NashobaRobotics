package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;

public class setHoodMinAngleCommand extends NRCommand
{
    public setHoodMinAngleCommand()
    {
        super(Hood.getInstance());
    }

    public void onStart()
    {
        Hood.getInstance().setAngle(Hood.lowerMost);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}