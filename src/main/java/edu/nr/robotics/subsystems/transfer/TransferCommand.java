package edu.nr.robotics.subsystems.transfer;
 
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Time;
import edu.wpi.first.wpilibj.Timer;
 
public class TransferCommand extends NRCommand {
 
    double transferPercent = 0.6;
    Time transferTime;
    double currentTime = 0;
    double start = 0;
 
    public TransferCommand(Time transferTime){
        super(Transfer.getInstance());
        this.transferTime = transferTime;        
    }
 
    public void onStart(){
        this.start = Timer.getFPGATimestamp();
        Transfer.getInstance().setMotorSpeedInPercent(transferPercent);
    }
 
    protected void onEnd(){
        Transfer.getInstance().setMotorSpeedInPercent(0);
    }
 
    protected boolean isFinishedNR(){
        currentTime = Timer.getFPGATimestamp();
        return (currentTime - start) > transferTime.get(Time.Unit.SECOND);
    }
}
 
