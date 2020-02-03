package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class SetIntakeSpeedCommand extends NRCommand
{

    public SetIntakeSpeedCommand()
    {
        super(Intake.getInstance());
    }

    public void onStart()
    {
        Intake.getInstance().setMotorSpeedRaw(Intake.INTAKE_PERCENT);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}