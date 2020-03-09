package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class TurretRightButtonCommand extends NRCommand
{
    public TurretRightButtonCommand()
    {
        super(Turret.getInstance());
    }

    @Override
    protected void onExecute()
    {
        Turret.getInstance().setMotorSpeedInPercent(-0.15);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }

    @Override
    protected void onEnd()
    {
        Turret.getInstance().setMotorSpeedInPercent(0);
    }
}