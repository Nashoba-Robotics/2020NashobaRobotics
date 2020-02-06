package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.climbdeploy.ClimbDeploy;
import edu.nr.robotics.subsystems.winch.Winch;

public class ClimbJoystickCommand extends NRCommand
{
    public ClimbJoystickCommand()
    {
        super(new NRSubsystem[] {ClimbDeploy.getInstance(), Winch.getInstance()});
    }

    public void onExecute()
    {
        if(OI.getInstance().getClimbTurn() > 0)
            ClimbDeploy.getInstance().setMotorSpeedRaw(OI.getInstance().getClimbTurn());
        else if(OI.getInstance().getClimbTurn() < 0)
            Winch.getInstance().setMotorSpeedRaw(OI.getInstance().getClimbTurn());
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}