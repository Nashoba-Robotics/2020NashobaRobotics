package edu.nr.robotics.subsystems.transfer;

import edu.nr.lib.commandbased.NRCommand;

public class ZeroBallCountCommand extends NRCommand
{
    @Override
    protected void onStart()
    {
        Transfer.ballCount = 0;
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}