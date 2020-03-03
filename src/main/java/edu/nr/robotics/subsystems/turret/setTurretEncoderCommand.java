package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class setTurretEncoderCommand extends NRCommand
{
    public setTurretEncoderCommand()
    {
        super(Turret.getInstance());
    }

    @Override
    protected void onStart()
    {
        Turret.getInstance().setMotorSpeedInPercent(0.4);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}