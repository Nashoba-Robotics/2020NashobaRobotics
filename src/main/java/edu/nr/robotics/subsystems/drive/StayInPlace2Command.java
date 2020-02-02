package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.OI;

public class StayInPlace2Command extends NRCommand
{
    public StayInPlace2Command()
    {

    }

    public void onStart()
    {
        Drive.getInstance().stayInPlaceOnStart2();
    }

    @Override
    public void onEnd()
    {
        Drive.getInstance().stayInPlaceOnEnd2();
    }

    @Override
    public boolean isFinishedNR()
    {
        return OI.getInstance().isPushModeEnabled();
    }

    public void disable() {}
}