package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;

public class SetTurretPercentCommand extends NRCommand {
    public SetTurretPercentCommand()
    {
        super(Turret.getInstance());
    }

    @Override
    protected void onStart()
    {
        Turret.getInstance().setMotorSpeedInPercent(Turret.goalPercent);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}