package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class IntakePukeCommand extends NRCommand
{
    public IntakePukeCommand()
    {
        super(Intake.getInstance());
    }

    @Override
    protected void onStart() 
    {
        Intake.getInstance().deployIntake();
    }

    @Override
    protected void onExecute()
    {

        if(Intake.getInstance().isIntakeDeployed())
            Intake.getInstance().setMotorSpeedRaw(Intake.PUKE_PERCENT);
            //change to time based
    }

    @Override
    protected void onEnd()
    {
        Intake.getInstance().setMotorSpeedRaw(0);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}