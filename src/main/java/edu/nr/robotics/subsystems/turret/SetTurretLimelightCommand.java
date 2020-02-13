package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.network.LimelightNetworkTable.Pipeline;
import edu.nr.lib.units.Angle;

public class SetTurretLimelightCommand extends NRCommand
{
    //this ones for constantly adjusting

    private Angle limeLightAngle;

    public SetTurretLimelightCommand()
    {
        super(Turret.getInstance());
        LimelightNetworkTable.getInstance().setPipeline(Pipeline.Target);
    }

    @Override
    protected void onExecute()
    {
        this.limeLightAngle = LimelightNetworkTable.getInstance().getHorizOffset();
        Turret.getInstance().setAngle(limeLightAngle.add(Turret.getInstance().getAngle()));
        System.out.println("limeLightAngle: " + limeLightAngle.get(Angle.Unit.DEGREE));
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}