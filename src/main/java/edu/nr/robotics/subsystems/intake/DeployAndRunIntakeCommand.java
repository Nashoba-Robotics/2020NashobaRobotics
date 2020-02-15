package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class DeployAndRunIntakeCommand extends NRCommand
{

    public DeployAndRunIntakeCommand()
    {
        super(Intake.getInstance());
    }

    public void onStart()
    {
        if(Intake.getInstance().isIntakeDeployed() == false)
            Intake.getInstance().deployIntake();
            Intake.getInstance().setMotorSpeedRaw(Intake.INTAKE_PERCENT);

    }

    public boolean isFinishedNR()
    {
        return true;
    }
}