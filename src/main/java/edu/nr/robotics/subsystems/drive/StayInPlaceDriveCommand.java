package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.OI;

public class StayInPlaceDriveCommand extends NRCommand
{
    public StayInPlaceDriveCommand()
    {
        super(Drive.getInstance());
    }

    @Override
    public void onExecute()
    {
        Drive.getInstance().stayInPlaceOnStart();
    }

    //Should also try overriding onEnd() and see if that works
    @Override
    public void end(boolean interrupted)
    {
        Drive.getInstance().stayInPlaceOnEnd();
    }

    @Override
    public boolean isFinishedNR()
    {
        return OI.getInstance().isStayInPlaceModeEnabled();
    }
}