package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Time;

public class FireCommand extends NRCommand
{
    public FireCommand()
    {
        super(Indexer.getInstance());
    }

    @Override
    protected void onExecute()
    {
        if(!OI.getInstance().getManualMode())
        {
            if(OI.getInstance().getAcquireTargetHeld())
            {
                if(Shooter.getInstance().getSpeedShooter1().get(Angle.Unit.ROTATION, Time.Unit.MINUTE) >= 0.9 * Shooter.SHOOT_SPEED.get(Angle.Unit.ROTATION, Time.Unit.MINUTE))
                {
                    Indexer.getInstance().setSpeed(Indexer.SHOOTING_SPEED);
                }
                else
                {
                    Indexer.getInstance().setMotorSpeedInPercent(0);
                }
            }
        }

        else
        {
            if(OI.getInstance().getShooterToggleHeld())
            {
                if(Shooter.getInstance().getSpeedShooter1().get(Angle.Unit.ROTATION, Time.Unit.MINUTE) >= 0.9 * Shooter.SHOOT_SPEED.get(Angle.Unit.ROTATION, Time.Unit.MINUTE))
                {
                    Indexer.getInstance().setSpeed(Indexer.SHOOTING_SPEED);
                }
                else
                {
                    Indexer.getInstance().setMotorSpeedInPercent(0);
                }
            }
        }
    }

    @Override
    protected void onEnd()
    {
        Indexer.getInstance().setMotorSpeedInPercent(0);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}