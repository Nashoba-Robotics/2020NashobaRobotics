package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.multicommands.States;
import edu.nr.robotics.multicommands.States.State;
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
        }
        else
        {
            Indexer.getInstance().setMotorSpeedInPercent(0);
        }
    }
    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}