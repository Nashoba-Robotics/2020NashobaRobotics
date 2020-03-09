package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class NRToggleRunIntakeCommand extends NRCommand{

    public NRToggleRunIntakeCommand(){
        super(Intake.getInstance());
    }

    @Override
    public void onExecute(){
        if(Intake.getInstance().isIntakeDeployed())
        {
            if(Intake.getInstance().getSetPercent() == 0)
                Intake.getInstance().setMotorSpeedRaw(Intake.INTAKE_PERCENT);
            else
                Intake.getInstance().setMotorSpeedRaw(0);
        }
    }

    @Override
    public boolean isFinishedNR(){
        return true;
    }

}