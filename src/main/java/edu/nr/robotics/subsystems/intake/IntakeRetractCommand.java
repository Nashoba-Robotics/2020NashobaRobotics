package edu.nr.robotics.subsystems.intake;

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
            Intake.getInstance().setMotorSpeedRaw(0);
            Intake.getInstance().deployIntake();
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}