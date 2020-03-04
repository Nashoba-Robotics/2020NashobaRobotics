package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.network.LimelightNetworkTable.Pipeline;
import edu.nr.lib.units.Angle;

public class SetTurretLimelightCommand extends NRCommand
{
    //this one's for constantly adjusting

    public static Angle limeLightAngle = Angle.ZERO;

    public SetTurretLimelightCommand()
    {
        super(Turret.getInstance());
        LimelightNetworkTable.getInstance().setPipeline(Pipeline.Target);
    }

    @Override
    protected void onExecute()
    {
        this.limeLightAngle = LimelightNetworkTable.getInstance().getHorizOffset();
        Turret.getInstance().setAngle((Turret.getInstance().getAngle().sub(limeLightAngle)));

    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}