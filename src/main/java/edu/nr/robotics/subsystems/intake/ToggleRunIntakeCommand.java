package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ToggleRunIntakeCommand extends CommandBase
{
    public ToggleRunIntakeCommand()
    {
        addRequirements(Intake.getInstance());
    }

    @Override
    public void execute()
    {
        System.out.println("Toggle Run Intake Motors has been called");
        if(Intake.getInstance().isIntakeDeployed())
        {
            if(Intake.getInstance().getSetPercent() == 0)
                Intake.getInstance().setMotorSpeedRaw(Intake.INTAKE_PERCENT);
            else
                Intake.getInstance().setMotorSpeedRaw(0);
        }
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}