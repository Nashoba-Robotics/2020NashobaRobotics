package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.transfer.Transfer;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.wpi.first.wpilibj.Timer;

public class ShootCommand extends NRCommand
{
    double current = 0;
    double lastBall = 9999999999999.0;
    public ShootCommand()
    //OLD?
    {
        super(new NRSubsystem[] {Hood.getInstance(), Turret.getInstance(), Transfer.getInstance(), Shooter.getInstance(), Indexer.getInstance()});
    }

    @Override
    protected void onStart()
    {
        /*
        Angle limeLightAngle = LimelightNetworkTable.getInstance().getHorizOffset();
        Turret.getInstance().setAngle(limeLightAngle.add(Turret.getInstance().getAngle()));
        */
        //Add hood stuff once HoodLimelightCommand is finished
    }

    @Override
    protected void onExecute()
    {
        Shooter.getInstance().setMotorSpeed(Shooter.SHOOT_SPEED);
        /*if(Shooter.getInstance().getSpeedShooter1().get(Angle.Unit.ROTATION, Time.Unit.MINUTE) >= 0.85 * Shooter.SHOOT_SPEED.get(Angle.Unit.ROTATION, Time.Unit.MINUTE)){
            Indexer.getInstance().setMotorSpeedInPercent(1);
            Transfer.getInstance().setMotorSpeedInPercent(Transfer.TRANSFER_PERCENT);
        } else{
            Indexer.getInstance().setMotorSpeedInPercent(0);
            Transfer.getInstance().setMotorSpeedInPercent(0);
        }*/

        Indexer.getInstance().setMotorSpeedInPercent(1);
        Transfer.getInstance().setMotorSpeedInPercent(Transfer.TRANSFER_PERCENT);

    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }

    @Override
    protected void onEnd()
    {
        Shooter.getInstance().setNeutralOutput();
        Transfer.ballCount = 0;
        Indexer.getInstance().setSpeed(Speed.ZERO);
    }
}