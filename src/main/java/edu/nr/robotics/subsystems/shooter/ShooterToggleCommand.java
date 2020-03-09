package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.OI;

public class ShooterToggleCommand extends NRCommand
{
    public ShooterToggleCommand()
    {
        super(Shooter.getInstance());
    }

    @Override
    protected void onExecute()
    {
        if(LimelightNetworkTable.getInstance().getDistance().get(Distance.Unit.FOOT) < 18)
        {
            if(Shooter.speedSetPointShooter.get(Angle.Unit.ROTATION, Time.Unit.MINUTE) == 0)
            {
                Shooter.getInstance().setMotorSpeed(Shooter.SHOOT_SPEED);
            }

            else
            {
                Shooter.getInstance().setNeutralOutput();
            }
        }
        else
        {
            if(Shooter.speedSetPointShooter.get(Angle.Unit.ROTATION, Time.Unit.MINUTE) == 0)
            {
                Shooter.getInstance().setMotorSpeed(new AngularSpeed(6000, Angle.Unit.ROTATION, Time.Unit.MINUTE));
            }

            else
            {
                Shooter.getInstance().setNeutralOutput();
            }
        }
    }

    @Override
    protected void onEnd()
    {
        //Shooter.getInstance().setNeutralOutput();
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}