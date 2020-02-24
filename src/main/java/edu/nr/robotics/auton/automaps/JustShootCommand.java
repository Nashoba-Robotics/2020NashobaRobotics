package edu.nr.robotics.auton.automaps;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.multicommands.AcquireTargetCommand;
import edu.nr.robotics.multicommands.ShootCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.EnableMotionProfile;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class JustShootCommand extends SequentialCommandGroup
{
    public JustShootCommand()
    {
        super(new NRCommand[] {new EnableMotionProfile(new Distance(5, Distance.Unit.FOOT), Drive.ONE_D_PROFILE_DRIVE_PERCENT, Drive.ONE_D_ACCEL_PERCENT), new AcquireTargetCommand(), new ShootCommand()});
    }
}