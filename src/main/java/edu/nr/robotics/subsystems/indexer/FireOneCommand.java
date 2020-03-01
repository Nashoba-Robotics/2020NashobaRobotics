package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;

public class FireOneCommand extends NRCommand
{
    public FireOneCommand()
    {
        super(Indexer.getInstance());
    }

    @Override
    protected void onStart()
    {

    }

    @Override
    protected void onExecute()
    {

    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}