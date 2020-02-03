package edu.nr.robotics.subsystems.bashbar;

import edu.nr.lib.commandbased.NRCommand;

public class RetractBashBarCommand extends NRCommand
{
    public RetractBashBarCommand()
    {
        super(BashBar.getInstance());
    }

    public void onStart()
    {
        if(BashBar.getInstance().isBashBarDeployed())
            BashBar.getInstance().retractBashBar();
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}