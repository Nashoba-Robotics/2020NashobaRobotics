package edu.nr.robotics.subsystems.climbdeploy;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;

public class setClimberPositionCommand extends NRCommand
{
    private Distance distance;

    public setClimberPositionCommand(Distance distance)
    {
        super(ClimbDeploy.getInstance());
        this.distance = distance;
    }

    public void onStart()
    {
        ClimbDeploy.getInstance().setPosition(distance);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}