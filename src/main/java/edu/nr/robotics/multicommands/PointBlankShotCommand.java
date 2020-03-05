package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.transfer.Transfer;
import edu.nr.robotics.subsystems.turret.Turret;

public class PointBlankShotCommand extends NRCommand
{
    public PointBlankShotCommand()
    {
        super(new NRSubsystem[] {Hood.getInstance(), Shooter.getInstance(), Turret.getInstance(), Indexer.getInstance(), Transfer.getInstance()});
    }

    @Override
    protected void onStart()
    {
        Hood.getInstance().setAngle(Hood.POINT_BLANK_SHOT_ANGLE);
        Shooter.getInstance().setMotorSpeed(Shooter.SHOOT_SPEED);
        Turret.getInstance().setAngle(Angle.ZERO);
    }

    @Override
    protected void onExecute()
    {
        Indexer.getInstance().setMotorSpeedInPercent(1);
        Transfer.getInstance().setMotorSpeedInPercent(Transfer.TRANSFER_PERCENT);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}