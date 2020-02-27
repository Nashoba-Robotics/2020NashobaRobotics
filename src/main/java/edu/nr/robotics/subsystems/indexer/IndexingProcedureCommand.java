package edu.nr.robotics.subsystems.indexer;
 

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.multicommands.States;
import edu.nr.robotics.multicommands.States.State;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.transfer.Transfer;
 
public class IndexingProcedureCommand extends NRCommand{
 
    public IndexingProcedureCommand(){
        super(Indexer.getInstance());
    }

    @Override
    protected void onStart(){

    }

    @Override
    protected void onExecute(){

        
        if(EnabledSubsystems.INDEXER_ENABLED){
        if(!(States.getState() == States.State.IndexerReadyToShoot))
        {
            if(States.getState() == States.State.IndexerStillIndexing)
            {
                Indexer.getInstance().setSpeed(Indexer.INDEXING_SPEED);
 
            }

            else if(States.getState() == States.State.ReadyToTransfer)
            {
                Indexer.getInstance().setSpeed(Indexer.INDEXING_SPEED);
            }
 
            else if(States.getState() == States.State.PreparingToTransfer)
            {
                Indexer.getInstance().setMotorSpeedInPercent(0);
            }
            
            else
            {
                Indexer.getInstance().setMotorSpeedInPercent(0);
            }
        }
        }
    }
    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}