package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class TurretLeftButtonCommand extends NRCommand
{
    public TurretLeftButtonCommand()
    {
        super(Turret.getInstance());
    }

    @Override
    protected void onExecute()
    {
        Turret.getInstance().setMotorSpeedInPercent(0.15);
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