package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class ToggleIntakeCommand extends NRCommand
{
    public ToggleIntakeCommand()
    {
        super(Intake.getInstance());
    }

    @Override
    protected void onStart()
    {
        if(Intake.getInstance().isIntakeDeployed())
            new RetractAndOffIntakeCommand();
        else
            new DeployAndRunIntakeCommand();
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}