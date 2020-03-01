package edu.nr.robotics.subsystems.transfer;

import edu.nr.lib.commandbased.NRCommand;

public class TransferPercentCommand extends NRCommand
{
    public TransferPercentCommand()
    {
        super(Transfer.getInstance());
    }

    @Override
    protected void onStart()
    {

    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}