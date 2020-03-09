package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.subsystems.climbdeploy.ClimbDeploy;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.winch.Winch;

public class ClimbCommand extends NRCommand
{
    public ClimbCommand()
    {
        super(Winch.getInstance());
    }

    @Override
    protected void onStart()
    {

        //ghetto sniper mode
        Drive.MOVE_JOYSTICK_MULTIPLIER = 0.4;
        Drive.TURN_JOYSTICK_MULTIPLIER = 0.4;
        //Change to POS PID
        Winch.getInstance().setMotorSpeedRaw(Winch.WINCH_PERCENT);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }

    @Override
    protected void onEnd()
    {
        Winch.getInstance().setMotorSpeedRaw(0);
    }
}