package edu.nr.robotics.auton.automaps;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;
import edu.nr.robotics.multicommands.ShootCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.EnableMotionProfile;
import edu.nr.robotics.subsystems.hood.SetHoodAngleCommand;
import edu.nr.robotics.subsystems.intake.IntakeToggleDeployCommand;
import edu.nr.robotics.subsystems.intake.NRToggleRunIntakeCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class TestingAuto extends SequentialCommandGroup {
   
    public TestingAuto() 
    {
        super(new NRCommand[] {new SetHoodAngleCommand(new Angle(41.75, Angle.Unit.DEGREE)), new ShootCommand(), new IntakeToggleDeployCommand(), new NRToggleRunIntakeCommand(), new EnableMotionProfile(new Distance(-6, Distance.Unit.FOOT), Drive.ONE_D_PROFILE_DRIVE_PERCENT, Drive.ONE_D_ACCEL_PERCENT)});
    }
}