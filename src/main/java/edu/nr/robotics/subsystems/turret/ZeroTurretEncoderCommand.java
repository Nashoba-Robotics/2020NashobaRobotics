package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class ZeroTurretEncoderCommand extends NRCommand
{
    public ZeroTurretEncoderCommand()
    {
        super(Turret.getInstance());
    }

    @Override
    protected void onStart()
    {
        Turret.getInstance().zeroEncoder();
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}