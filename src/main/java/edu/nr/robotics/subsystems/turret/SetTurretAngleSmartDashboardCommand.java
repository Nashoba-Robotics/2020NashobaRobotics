package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class SetTurretAngleSmartDashboardCommand extends NRCommand
{
    public SetTurretAngleSmartDashboardCommand()
    {
        super(Turret.getInstance());
    }

    @Override
    public void onStart()
    {
    //    System.out.println("setTurret command was called: " + Turret.goalAngle.get(Angle.Unit.DEGREE));
        Turret.getInstance().setAngle(Turret.goalAngle);
    }

    @Override
    public boolean isFinishedNR()
    {
        return true;
    }
}