package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.subsystems.climbdeploy.ClimbDeploy;
import edu.nr.robotics.subsystems.winch.Winch;

public class ClimbCommand extends NRCommand
{
    public ClimbCommand()
    {
        super(new NRSubsystem[] {ClimbDeploy.getInstance(), Winch.getInstance()});
    }

    public void onStart()
    {
        ClimbDeploy.getInstance().setMotorSpeedRaw(ClimbDeploy.RETRACT_PERCENT);
        Winch.getInstance().setMotorSpeedRaw(Winch.WINCH_PERCENT);
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}