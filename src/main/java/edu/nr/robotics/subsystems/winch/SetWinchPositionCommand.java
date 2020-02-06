package edu.nr.robotics.subsystems.winch;

import edu.nr.lib.commandbased.NRCommand;

public class SetWinchPositionCommand extends NRCommand
{
    public SetWinchPositionCommand()
    {
        super(Winch.getInstance());
    }

    public void onStart()
    {
        Winch.getInstance().setPosition(Winch.goalPositionClimb);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}