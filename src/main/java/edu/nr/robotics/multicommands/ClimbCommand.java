package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.subsystems.climbdeploy.ClimbDeploy;
import edu.nr.robotics.subsystems.winch.Winch;

public class ClimbCommand extends NRCommand
{
    public ClimbCommand()
    {
        super(new NRSubsystem[] {ClimbDeploy.getInstance(), Winch.getInstance()});
    }

    @Override
    protected void onStart()
    {
        //Change to POS PID
        ClimbDeploy.getInstance().setMotorSpeedRaw(ClimbDeploy.RETRACT_PERCENT);
        Winch.getInstance().setPosition(new Distance(5, Distance.Unit.INCH));
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }

    @Override
    protected void onEnd()
    {
        ClimbDeploy.getInstance().setMotorSpeedRaw(0);
        Winch.getInstance().setMotorSpeedRaw(0);
    }
}