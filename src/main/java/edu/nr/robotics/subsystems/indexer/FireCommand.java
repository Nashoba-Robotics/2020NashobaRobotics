package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.shooter.Shooter;

public class FireCommand extends NRCommand {
    public FireCommand() {
        super(Indexer.getInstance());
    }

    @Override
    protected void onExecute() {

        if (Shooter.getInstance().getSpeedShooter1().get(Angle.Unit.ROTATION, Time.Unit.MINUTE) > 0.94 * Shooter.SHOOT_SPEED.get(Angle.Unit.ROTATION, Time.Unit.MINUTE)) {
            Indexer.getInstance().setMotorSpeedInPercent(1);
        }
    }

    @Override
    protected void onEnd() {
        Indexer.getInstance().setMotorSpeedInPercent(0);
    }

    @Override
    protected boolean isFinishedNR() {
        return false;
    }
}