package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class SetTurretAngleCommand extends NRCommand{

    private Angle targetAngle;

    public SetTurretAngleCommand(Angle turnToAngle)
    {
        super(Turret.getInstance());
        targetAngle = turnToAngle;
    }

    @Override
    protected void onStart()
    {
        Turret.getInstance().setAngle(targetAngle);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }

}