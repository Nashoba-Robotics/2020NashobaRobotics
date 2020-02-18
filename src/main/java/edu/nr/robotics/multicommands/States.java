package edu.nr.robotics.multicommands;

import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.transfer.Transfer;

public class States
{
    public States()
    {
        
    }

    public static enum State
    {
        IndexerReadyForBall, IndexerStillIndexing, IndexerReadyToShoot, IndexerAndTransferEmpty, ReadyToTransfer, UhOhNoClue
    }

    public static State getState()
    {
        if(Indexer.getInstance().continueMoving(Transfer.getInstance().getNumberOfBalls()))
        {
            return State.IndexerStillIndexing;
        }

        else if(!Indexer.getInstance().continueMoving(Transfer.getInstance().getNumberOfBalls()) && Transfer.getInstance().hasBall())
        {
           return State.ReadyToTransfer; 
        }

        else if(!Indexer.getInstance().continueMoving(Transfer.getInstance().getNumberOfBalls()) && !Transfer.getInstance().hasBall())
        {
            return State.IndexerReadyForBall;
        }

        else if(Indexer.getInstance().readyToShoot())
        {
            return State.IndexerReadyToShoot;
        }

        else if(Transfer.getInstance().getNumberOfBalls() == 0 && !Transfer.getInstance().hasBall())
        {
            return State.IndexerAndTransferEmpty;
        }

        return State.UhOhNoClue;
    }
}