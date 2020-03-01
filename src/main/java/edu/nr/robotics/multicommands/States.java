package edu.nr.robotics.multicommands;

import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.transfer.Transfer;

public class States
{
    public States()
    {
        
    }

    public static enum State
    {
        IndexerReadyForBall, IndexerStillIndexing, IndexerReadyToShoot, ReadyToTransfer, UhOhNoClue, PreparingToTransfer
    }

    public static State getState()
    {
        if(EnabledSubsystems.TRANSFER_ENABLED && EnabledSubsystems.INDEXER_ENABLED){
        if(Indexer.getInstance().readyToShoot()){
            return State.IndexerReadyToShoot;
        }
        else if(Indexer.getInstance().continueMoving(Transfer.getInstance().getNumberOfBalls())){
            return State.IndexerStillIndexing;
        }
        
        else if(!Indexer.getInstance().continueMoving(Transfer.getInstance().getNumberOfBalls()) && Transfer.getInstance().hasBall())
        {
            return State.ReadyToTransfer;
        }

        else if(!Transfer.getInstance().hasBall() && !Indexer.getInstance().continueMoving(Transfer.getInstance().getNumberOfBalls())){
            return State.PreparingToTransfer;
        }
        return State.UhOhNoClue;
        }
        
        return State.UhOhNoClue;
    }

}