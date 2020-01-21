package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class SetTurretAngleSmartDashboardCommand extends NRCommand
{
    public SetTurretAngleSmartDashboardCommand()
    {
        super(Turret.getInstance());
    }

    @Override
    public void onStart()
    {
        Turret.getInstance().setAngle(Turret.goalAngle);
    }

    @Override
    public boolean isFinishedNR()
    {
        return true;
    }
}