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
        
        Shooter.getInstance().setMotorSpeedInPercent( - 1 * Shooter.RUN_PERCENT);
    }
 
    public void onExecute(){
    }
 
    public boolean isFinishedNR(){
        return true;
    }
 
    protected void onEnd(){
    }
}
 
 
