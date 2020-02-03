package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class RetractAndOffIntakeCommand extends NRCommand
{
    public RetractAndOffIntakeCommand()
    {
        super(Intake.getInstance());
    }

    public void onStart()
    {
        if(Intake.getInstance().isIntakeDeployed() == true)
            Intake.getInstance().retractIntake();
        Intake.getInstance().setMotorSpeedRaw(0);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}