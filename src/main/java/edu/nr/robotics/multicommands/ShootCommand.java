package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Speed;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.turret.Turret;

public class ShootCommand extends NRCommand
{
    public ShootCommand()
    {
        super(new NRSubsystem[] {Hood.getInstance(), Turret.getInstance(), Shooter.getInstance(), Indexer.getInstance()});
    }

    @Override
    protected void onStart()
    {
        Shooter.getInstance().setMotorSpeed(Shooter.SHOOT_SPEED);

        Angle limeLightAngle = LimelightNetworkTable.getInstance().getHorizOffset();
        Turret.getInstance().setAngle(limeLightAngle.add(Turret.getInstance().getAngle()));

        //Add hood stuff once HoodLimelightCommand is finished
    }

    @Override
    protected void onExecute()
    {
        Indexer.getInstance().setSpeed(Indexer.SHOOTING_SPEED);
    }

    @Override
    protected boolean isFinishedNR()
    {
        //Logic needs to be changed
        return true;
    }

    @Override
    protected void onEnd()
    {
        Shooter.getInstance().setMotorSpeed(AngularSpeed.ZERO);
        Indexer.getInstance().setSpeed(Speed.ZERO);
    }
}