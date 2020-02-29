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
            //System.out.println("Ready To Shoot");
            return State.IndexerReadyToShoot;
        }
        else if(Indexer.getInstance().continueMoving(Transfer.getInstance().getNumberOfBalls())){
            //System.out.println("Indexer Still Indexing");
            return State.IndexerStillIndexing;
        }
        
        else if(!Indexer.getInstance().continueMoving(Transfer.getInstance().getNumberOfBalls()) && Transfer.getInstance().hasBall())
        {
            //System.out.println("Ready To Transfer");
            return State.ReadyToTransfer;
        }

        else if(!Transfer.getInstance().hasBall() && !Indexer.getInstance().continueMoving(Transfer.getInstance().getNumberOfBalls())){
            //System.out.println("Preparing to Transfer");
            return State.PreparingToTransfer;
        }
        return State.UhOhNoClue;
        }
        
        return State.UhOhNoClue;
    }

}