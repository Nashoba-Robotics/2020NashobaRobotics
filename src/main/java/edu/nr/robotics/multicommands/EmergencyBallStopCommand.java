package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.intake.Intake;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.transfer.Transfer;
import edu.nr.robotics.subsystems.turret.Turret;

public class EmergencyBallStopCommand extends NRCommand
{
    public EmergencyBallStopCommand()
    {
        super(new NRSubsystem[] {Intake.getInstance(), Indexer.getInstance(), Shooter.getInstance(), Hood.getInstance(), Transfer.getInstance(), Turret.getInstance()});
    }

    @Override
    protected void onExecute()
    {
        Hood.getInstance().setAngle(Angle.ZERO);
        Intake.getInstance().disable();
        Indexer.getInstance().disable();
        Shooter.getInstance().disable();
        Transfer.getInstance().disable();
        Turret.getInstance().disable();
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}