package edu.nr.robotics.subsystems.transfer;
 
import edu.nr.lib.commandbased.NRCommand;
 
public class TransferSetSpeedCommand extends NRCommand{
 
    private double targetSpeed;

    public TransferSetSpeedCommand(double speed){
        super(Transfer.getInstance());
        targetSpeed = speed;
    }
 
    protected void onStart(){
        Transfer.getInstance().setMotorSpeedInPercent(targetSpeed);
    }
 
    protected boolean isFinishedNR(){
        return true;
    }
}
 
