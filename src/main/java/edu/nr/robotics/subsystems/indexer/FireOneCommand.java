package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.nr.robotics.subsystems.shooter.Shooter;
import jdk.jfr.Enabled;

public class FireOneCommand extends NRCommand
{
    boolean previousValue = EnabledSensors.getInstance().indexerShooterSensor.get();

    public FireOneCommand()
    {
        super(Indexer.getInstance());
    }

    @Override
    protected void onStart()
    {
        Shooter.getInstance().setMotorSpeed(Shooter.SHOOT_SPEED);
    }

    @Override
    protected void onExecute()
    {
        if(Shooter.getInstance().getSpeedShooter1().mul(1.1).greaterThan(Shooter.SHOOT_SPEED))
            Indexer.getInstance().setMotorSpeedInPercent(1);
    }

    @Override
    protected void onEnd() 
    {
        Shooter.getInstance().setNeutralOutput();
    }

    @Override
    protected boolean isFinishedNR()
    {
        if(previousValue != EnabledSensors.getInstance().indexerShooterSensor.get() && !EnabledSensors.getInstance().indexerShooterSensor.get())
            return true;
        previousValue = EnabledSensors.getInstance().indexerShooterSensor.get();
        return false;
    }
}