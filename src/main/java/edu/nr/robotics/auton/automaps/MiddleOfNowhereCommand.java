package edu.nr.robotics.auton.automaps;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.EnableMotionProfile;

public class MiddleOfNowhereCommand extends NRCommand
{
    public MiddleOfNowhereCommand()
    {
        super(Drive.getInstance());
    }

    @Override
    protected void onStart()
    {
        new EnableMotionProfile(new Distance(5, Distance.Unit.FOOT), Drive.ONE_D_PROFILE_DRIVE_PERCENT, Drive.ONE_D_ACCEL_PERCENT);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}