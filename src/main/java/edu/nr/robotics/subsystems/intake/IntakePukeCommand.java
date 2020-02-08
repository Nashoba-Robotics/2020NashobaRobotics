package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class IntakePukeCommand extends NRCommand
{
    public IntakePukeCommand()
    {
        super(Intake.getInstance());
    }

    public void onStart()
    {
        if(Intake.getInstance().isIntakeDeployed() == true)
            Intake.getInstance().setMotorSpeedRaw(-1 * Intake.INTAKE_PERCENT); 
            //change to time based ?
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}