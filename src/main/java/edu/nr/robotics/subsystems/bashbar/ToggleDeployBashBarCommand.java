package edu.nr.robotics.subsystems.bashbar;

import edu.nr.lib.commandbased.NRCommand;

public class ToggleDeployBashBarCommand extends NRCommand
{
    public ToggleDeployBashBarCommand()
    {
        super(BashBar.getInstance());
    }

    public void onStart()
    {
        if(BashBar.getInstance().isBashBarDeployed())
            BashBar.getInstance().retractBashBar();
        else
            BashBar.getInstance().deployBashBar();
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}