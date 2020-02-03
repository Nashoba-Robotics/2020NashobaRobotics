package edu.nr.robotics.subsystems.shooter;
 
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Time;
import edu.wpi.first.wpilibj.Timer;
 
public class ShooterPukeCommand extends NRCommand{
    double start = 0;
    double currentTime = 0;
 
    public ShooterPukeCommand(){
        super(Shooter.getInstance());
    }
 
    public void onStart(){
        start = Timer.getFPGATimestamp();
        Shooter.getInstance().setMotorSpeedInPercent( - 1 * Shooter.RUN_PERCENT);
    }
 
    public void onExecute(){
        currentTime = Timer.getFPGATimestamp();
    }
 
    public boolean isFinishedNR(){
        return (currentTime - start) > Shooter.PUKE_TIME.get(Time.Unit.SECOND);
    }
 
    protected void onEnd(){
        Shooter.getInstance().setMotorSpeedInPercent(0);
    }
}
 
 
