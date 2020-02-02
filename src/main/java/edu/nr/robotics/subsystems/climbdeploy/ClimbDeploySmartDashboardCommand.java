package edu.nr.robotics.subsystems.climbdeploy;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Distance.Unit;

public class ClimbDeploySmartDashboardCommand extends NRCommand
{
    public ClimbDeploySmartDashboardCommand()
    {
        super(ClimbDeploy.getInstance());
    }

    public void onStart()
    {
        ClimbDeploy.getInstance().setPosition(ClimbDeploy.goalPositionClimb);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}