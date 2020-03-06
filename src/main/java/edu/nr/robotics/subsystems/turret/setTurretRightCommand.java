package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class setTurretRightCommand extends NRCommand
{
    public setTurretRightCommand()
    {
        super(Turret.getInstance());
    }

    public void onStart()
    {
        Turret.getInstance().setAngle(Turret.RIGHT_ANGLE);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}