package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.units.Angle;

public class TurretLimelightCommand extends NRCommand
{

    public static Angle limeLightAngle = Angle.ZERO;

    public TurretLimelightCommand()
    {
        super(Turret.getInstance());
        this.limeLightAngle = LimelightNetworkTable.getInstance().getHorizOffset();
    }

    @Override
    protected void onStart()
    {
        Turret.getInstance().setAngle((Turret.getInstance().getAngle().sub(limeLightAngle)));
    }

    @Override
    protected void onExecute() {
        
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}