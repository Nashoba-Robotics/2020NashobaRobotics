package edu.nr.robotics.subsystems.climbdeploy;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;

public class ClimbDeployCommand extends NRCommand
{
    private Distance targetHeight;

    public ClimbDeployCommand(Distance targetHeight)
    {
        super(ClimbDeploy.getInstance());
        this.targetHeight = targetHeight;
    }

    public void onStart()
    {
        ClimbDeploy.getInstance().setPosition(targetHeight);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}