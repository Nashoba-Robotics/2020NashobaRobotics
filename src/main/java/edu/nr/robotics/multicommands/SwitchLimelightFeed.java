package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.network.LimelightNetworkTable.Pipeline;

public class SwitchLimelightFeed extends NRCommand
{
    public SwitchLimelightFeed()
    {

    }

    @Override
    protected void onStart()
    {
        LimelightNetworkTable.getInstance().setPipeline(Pipeline.DriverCam);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}