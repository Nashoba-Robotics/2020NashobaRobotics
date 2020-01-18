package edu.nr.robotics.subsystems.sparkIntake;

import edu.nr.lib.commandbased.NRCommand;


public class IntakeRetractCommand extends NRCommand
{
    public IntakeRetractCommand()
    {
        super(Intake.getInstance());
    }

    public void onStart()
    {
        if(Intake.getInstance().isIntakeDeployed() == true)
            Intake.getInstance().deployIntake();
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}