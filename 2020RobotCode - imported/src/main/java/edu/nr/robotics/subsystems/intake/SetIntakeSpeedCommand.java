package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class SetIntakeSpeedCommand extends NRCommand
{
    private double targetSpeed;

    public SetIntakeSpeedCommand(double targetSpeed)
    {
        super(Intake.getInstance());
        this.targetSpeed = targetSpeed;
    }

    public void onStart()
    {
        Intake.getInstance().setMotorSpeedRaw(targetSpeed);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}