package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.units.Angle;

public class TurretLimelightCommand extends NRCommand
{

    private Angle limeLightAngle;

    public TurretLimelightCommand()
    {
        super(Turret.getInstance());
        this.limeLightAngle = LimelightNetworkTable.getInstance().getHorizOffset();
    }

    @Override
    protected void onStart()
    {
        Turret.getInstance().setAngle(limeLightAngle.add(Turret.getInstance().getAngle()));
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}