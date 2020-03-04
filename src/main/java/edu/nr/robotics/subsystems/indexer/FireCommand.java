package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.OI;

public class FireCommand extends NRCommand
{
    public FireCommand()
    {
        super(Indexer.getInstance());
    }

    @Override
    protected void onExecute()
    {
        if(!OI.getInstance().getManualMode())
        {
            if(OI.getInstance().getAcquireTargetHeld())
            {
                Indexer.getInstance().setMotorSpeedInPercent(1);
            }
        }

        else
        {
            if(OI.getInstance().getShooterToggleHeld())
            {
                Indexer.getInstance().setMotorSpeedInPercent(1);
            }
        }
    }

    @Override
    protected void onEnd()
    {
        Indexer.getInstance().setMotorSpeedInPercent(0);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}