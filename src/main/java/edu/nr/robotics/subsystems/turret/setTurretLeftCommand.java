package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class setTurretLeftCommand extends NRCommand
{
    public setTurretLeftCommand()
    {
        super(Turret.getInstance());
    }

    public void onStart()
    {
        Turret.getInstance().setAngle(new Angle(-0.25, Angle.Unit.ROTATION));
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}