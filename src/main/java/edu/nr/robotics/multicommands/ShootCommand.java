package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Speed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.turret.Turret;
import edu.wpi.first.wpilibj.Timer;

public class ShootCommand extends NRCommand
{
    double current = 0;
    double lastBall = 9999999999999.0;
    public ShootCommand()
    {
        super(new NRSubsystem[] {Hood.getInstance(), Turret.getInstance(), Shooter.getInstance(), Indexer.getInstance()});
    }

    @Override
    protected void onStart()
    {
        Shooter.getInstance().setMotorSpeed(Shooter.SHOOT_SPEED);
        /*
        Angle limeLightAngle = LimelightNetworkTable.getInstance().getHorizOffset();
        Turret.getInstance().setAngle(limeLightAngle.add(Turret.getInstance().getAngle()));
        */
        //Add hood stuff once HoodLimelightCommand is finished
    }

    @Override
    protected void onExecute()
    {
        if(Shooter.getInstance().getSpeedShooter1().get(Angle.Unit.DEGREE, Time.Unit.SECOND) >= Shooter.SHOOT_SPEED.mul(0.9).get(Angle.Unit.DEGREE, Time.Unit.SECOND)){
            Indexer.getInstance().setSpeed(Indexer.SHOOTING_SPEED);
        }
    }

    @Override
    protected boolean isFinishedNR()
    {
        current = Timer.getFPGATimestamp();
        
        //Logic needs to be changed
        
        if(EnabledSensors.getInstance().indexerShooterSensor.get()){
            lastBall = Timer.getFPGATimestamp();
        }
        
        
        if(current - lastBall > 0.5){ // half a second passes, no ball to shoot, we've run out and should stop making loud shooter noises
            return true;
        }
        return false;
    }

    @Override
    protected void onEnd()
    {
        Shooter.getInstance().setMotorSpeed(AngularSpeed.ZERO);
        Indexer.getInstance().setSpeed(Speed.ZERO);
    }
}