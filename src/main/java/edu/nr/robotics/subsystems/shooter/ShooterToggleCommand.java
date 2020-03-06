package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.AngularSpeed;
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
        if(OI.getInstance().getManualMode())
            Shooter.getInstance().setMotorSpeed(Shooter.SHOOT_SPEED);
    }

    @Override
    protected void onEnd()
    {
        Shooter.getInstance().setNeutralOutput();
    }

    @Override
    protected boolean isFinishedNR()
    {
        if(!OI.getInstance().getManualMode())
            return true;
        return false;
    }
}