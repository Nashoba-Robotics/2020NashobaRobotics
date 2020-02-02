package edu.nr.robotics.subsystems.transfer;
 
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.AngularSpeed;
 
public class TransferSetSpeedCommand extends NRCommand{
 
    private AngularSpeed targetSpeed;
 
    public TransferSetSpeedCommand(AngularSpeed speed){
        super(Transfer.getInstance());
        targetSpeed = speed;
        //RIP Kobe Bryant, Nick has big calves
    }
 
    protected void onStart(){
        Transfer.getInstance().setSpeed(targetSpeed);
    }
 
    protected boolean isFinishedNR(){
        return true;
    }
}
 
