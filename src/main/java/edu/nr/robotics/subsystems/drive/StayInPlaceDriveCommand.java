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
    public void onStart()
    {
        Drive.getInstance().stayInPlaceOnStart();
    }

    @Override
    protected void onExecute() 
    {
        Drive.getInstance().stayInPlaceOnExecute();
    }

    @Override
    protected void onEnd(boolean interrupted)
    {
        Drive.getInstance().stayInPlaceOnEnd();
    }

    @Override
    public boolean isFinishedNR()
    {
        return OI.getInstance().isStayInPlaceModeEnabled();
    }
}