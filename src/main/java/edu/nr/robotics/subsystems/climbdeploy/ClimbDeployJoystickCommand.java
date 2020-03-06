package edu.nr.robotics.subsystems.climbdeploy;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.OI;

public class ClimbDeployJoystickCommand extends NRCommand
{
    public ClimbDeployJoystickCommand()
    {
        super(ClimbDeploy.getInstance());
    }

    @Override
    protected void onExecute()
    {
        if(OI.climbMode)
        {
            ClimbDeploy.getInstance().setMotorSpeedRaw(OI.getInstance().getOperatorLeftTurn());
        }
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }

    public boolean shouldSwitchToJoystick() 
    {
        return OI.getInstance().isOperatorLeftNonZero();
    }
}