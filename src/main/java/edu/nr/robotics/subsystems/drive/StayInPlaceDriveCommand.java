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

    public void onEnd()
    {
        Drive.getInstance().stayInPlaceOnEnd();
    }

    public boolean isFinishedNR()
    {
        return OI.getInstance().isPushModeEnabled();
    }
}