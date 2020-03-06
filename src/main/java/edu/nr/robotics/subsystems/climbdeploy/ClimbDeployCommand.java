package edu.nr.robotics.subsystems.climbdeploy;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.OI;

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
        OI.climbMode = true;
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}