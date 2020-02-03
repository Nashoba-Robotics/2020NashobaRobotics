package edu.nr.robotics.subsystems.transfer;
 
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Time;
import edu.wpi.first.wpilibj.Timer;
 
public class TransferPukeCommand extends NRCommand{
    double start = 0;
    double currentTime = 0;
 
    public TransferPukeCommand(){
        super(Transfer.getInstance());
    }
 
    protected void onStart(){
        start = Timer.getFPGATimestamp();
        Transfer.getInstance().setMotorSpeedInPercent(Transfer.PUKE_PERCENT);
    }
 
    protected void onExecute(){
        currentTime = Timer.getFPGATimestamp();
    }
    
    protected void onEnd(){
        Transfer.getInstance().setMotorSpeedInPercent(0);
    }
 
    protected boolean isFinishedNR(){
        return (currentTime - start) > Transfer.PUKE_TIME.get(Time.Unit.SECOND);
    }
}