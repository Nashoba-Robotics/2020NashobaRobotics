package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.OI;

public class StayInPlaceDriveCommand extends NRCommand
{
    public StayInPlaceDriveCommand()
    {
        
    }

    public void onStart()
    {
        Drive.getInstance().stayInPlaceOnStart();
    }

    public void onExecute()
    {
        Drive.getInstance().stayInPlace();
    }

    public boolean isFinishedNR()
    {
        //Change to when button is released
        return false;
    }
}