package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class SetHoodAngleSmartDashboardCommand extends NRCommand
{   
    public SetHoodAngleSmartDashboardCommand()
    {
        super(Hood.getInstance());
    }

    public void onStart()
    {
        Hood.getInstance().setAngle(Hood.goalAngleHood);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}