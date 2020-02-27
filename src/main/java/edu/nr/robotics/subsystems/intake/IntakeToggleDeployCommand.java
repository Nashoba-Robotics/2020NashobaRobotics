package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class IntakeToggleDeployCommand extends NRCommand
{
    public IntakeToggleDeployCommand()
    {
        super(Intake.getInstance());
    }

    public void onStart()
    {
        if(Intake.getInstance().isIntakeDeployed() == false)
            Intake.getInstance().deployIntake();
        else
        {
            Intake.getInstance().setMotorSpeedRaw(0);
            Intake.getInstance().retractIntake();
        }
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}