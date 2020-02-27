package edu.nr.robotics.subsystems.transfer;
 
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.multicommands.States;
import edu.nr.robotics.multicommands.States.State;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.transfer.Transfer;
 
public class TransferProcedureCommand extends NRCommand{
 
    public TransferProcedureCommand(){
        super(Transfer.getInstance());
    }
 
    public void onStart(){
 
    }
 
    public void onExecute(){
 
        ///maybe setmotor speeds for vPID, ensure consistent performance
        /*
        if(Indexer.getInstance().continueMoving()){
            Indexer.getInstance().setMotorSpeedInPercent(0.7);
        }
        if(Transfer.getInstance().hasBall() && !Indexer.getInstance().continueMoving()){
            Transfer.getInstance().setMotorSpeedInPercent(.9);
            //wait time for ball to get to indexer?
            Indexer.getInstance().setPosition(Indexer.getInstance().getPosition().add(Indexer.SHOOT_ONE_DISTANCE));
            //count balls indexed? how count when shot?
        }
        if(Indexer.getInstance().readyToShoot()){
            Indexer.getInstance().setMotorSpeedInPercent(0);
            Transfer.getInstance().setMotorSpeedInPercent(0);
        }
        */
        if(EnabledSubsystems.TRANSFER_ENABLED){
        if(!(States.getState() == States.State.IndexerReadyToShoot))
        {
            if(States.getState() == States.State.IndexerStillIndexing)
            {
                Transfer.getInstance().setMotorSpeedInPercent(0);
            }
 
            else if(States.getState() == States.State.ReadyToTransfer)
            {
                Transfer.getInstance().setMotorSpeedInPercent(0);
            }
 
            else if(States.getState() == States.State.PreparingToTransfer)
            {
                Transfer.getInstance().setMotorSpeedInPercent(0.6);
            }
        }
        else
        {
            Transfer.getInstance().setMotorSpeedInPercent(0);
        }
    }
    }
    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}
 
 
