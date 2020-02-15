package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class ToggleRunIntakeCommand extends NRCommand
{
    public ToggleRunIntakeCommand()
    {
        super(Intake.getInstance());
    }

    public void onStart()
    {
        if(Intake.getInstance().isIntakeDeployed())
        {
            if(Intake.getInstance().getIdealMotorSpeed() == 0)
                Intake.getInstance().setMotorSpeedRaw(Intake.INTAKE_PERCENT);
            else
                Intake.getInstance().setMotorSpeedRaw(0);
        }
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}