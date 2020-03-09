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
    protected void onStart()
    {
     //   System.out.println("setTurretAngle command was called: " + Turret.goalAngle.get(Angle.Unit.DEGREE));
        Turret.getInstance().setAngle(Turret.goalAngle);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}