package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;


public class IntakeExtendCommand extends NRCommand
{
    public IntakeExtendCommand()
    {
        super(Intake.getInstance());
    }

    public void onStart()
    {
        if(Intake.getInstance().isIntakeDeployed() == false)
            Intake.getInstance().deployIntake();
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}