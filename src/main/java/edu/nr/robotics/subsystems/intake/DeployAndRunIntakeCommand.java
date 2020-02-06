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
        if(Intake.getInstance().isIntakeDeployed())
            Intake.getInstance().setMotorSpeedRaw(Intake.getInstance().INTAKE_PERCENT);

    }

    public boolean isFinishedNR()
    {
        return true;
    }
}