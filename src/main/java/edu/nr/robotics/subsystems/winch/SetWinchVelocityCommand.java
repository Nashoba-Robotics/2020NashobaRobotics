package edu.nr.robotics.subsystems.winch;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Speed;

public class SetWinchVelocityCommand extends NRCommand
{
    private Speed targetSpeed;

    public SetWinchVelocityCommand(Speed toSpeed)
    {
        super(Winch.getInstance());
        targetSpeed = toSpeed;
    }

    @Override
    protected void onStart()
    {
        Winch.getInstance().setMotorSpeed(targetSpeed);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}