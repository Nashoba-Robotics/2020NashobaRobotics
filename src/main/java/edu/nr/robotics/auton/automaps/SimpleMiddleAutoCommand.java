package edu.nr.robotics.auton.automaps;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.multicommands.AcquireTargetCommand;
import edu.nr.robotics.multicommands.ShootCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.EnableMotionProfile;
import edu.nr.robotics.subsystems.drive.TurnToAngleCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class SimpleMiddleAutoCommand extends SequentialCommandGroup {
   
    public SimpleMiddleAutoCommand() 
    {
        super(new NRCommand[] {new TurnToAngleCommand(new Angle(30, Angle.Unit.DEGREE)), new EnableMotionProfile(new Distance(3, Distance.Unit.FOOT), Drive.ONE_D_PROFILE_DRIVE_PERCENT, Drive.ONE_D_ACCEL_PERCENT), new AcquireTargetCommand(), new ShootCommand()});
    }
}