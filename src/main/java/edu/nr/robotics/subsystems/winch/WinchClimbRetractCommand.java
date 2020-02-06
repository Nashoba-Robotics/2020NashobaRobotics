package edu.nr.robotics.subsystems.winch;

import edu.nr.lib.commandbased.NRCommand;

public class WinchClimbRetractCommand extends NRCommand
{
    public WinchClimbRetractCommand()
    {
        super(Winch.getInstance());
    }

    public void onStart()
    {
        Winch.getInstance().setMotorSpeedRaw(Winch.WINCH_PERCENT);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}