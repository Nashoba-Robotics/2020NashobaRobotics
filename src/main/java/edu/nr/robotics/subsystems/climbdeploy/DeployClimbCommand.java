package edu.nr.robotics.subsystems.climbdeploy;

import edu.nr.lib.commandbased.NRCommand;

public class DeployClimbCommand extends NRCommand
{
    public DeployClimbCommand()
    {
        super(ClimbDeploy.getInstance());
    }

    public void onStart()
    {
        ClimbDeploy.getInstance().setPosition(ClimbDeploy.DEPLOY_DISTANCE);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}