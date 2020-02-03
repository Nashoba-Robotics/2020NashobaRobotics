package edu.nr.robotics.subsystems.bashbar;

import edu.nr.lib.commandbased.NRCommand;

public class DeployBashBarCommand extends NRCommand
{
    public DeployBashBarCommand()
    {
        super(BashBar.getInstance());
    }

    public void onStart()
    {
        if(!BashBar.getInstance().isBashBarDeployed())
            BashBar.getInstance().deployBashBar();
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}