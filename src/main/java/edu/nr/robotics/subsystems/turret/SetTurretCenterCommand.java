package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class SetTurretCenterCommand extends NRCommand
{
    public SetTurretCenterCommand()
    {
        super(Turret.getInstance());
    }

    @Override
    protected void onExecute()
    {
        Turret.getInstance().setAngle(Turret.MID_ANGLE);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}