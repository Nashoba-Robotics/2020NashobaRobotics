package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.multicommands.States.State;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.transfer.Transfer;

public class IndexingProcedureCommand extends NRCommand{

    public IndexingProcedureCommand(){
        super(new NRSubsystem []{Transfer.getInstance(), Indexer.getInstance()});
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
        if(!Indexer.getInstance().readyToShoot())
        {
            if(States.getState() == States.State.IndexerStillIndexing)
            {
                Indexer.getInstance().setMotorSpeedInPercent(0.8);
                Transfer.getInstance().setMotorSpeedInPercent(0);
            }

            else if(States.getState() == States.State.IndexerAndTransferEmpty)
            {
                Indexer.getInstance().setMotorSpeedInPercent(0);
                Transfer.getInstance().setMotorSpeedInPercent(0);
            }

            else if(States.getState() == States.State.IndexerReadyForBall)
            {
                Transfer.getInstance().setMotorSpeedInPercent(0.7);
                Indexer.getInstance().setMotorSpeedInPercent(0.8);
            }

            else if(States.getState() == States.State.ReadyToTransfer)
            {
                Indexer.getInstance().setMotorSpeedInPercent(0.8);
                Transfer.getInstance().setMotorSpeedInPercent(0.7);
            }
        }
        else
        {
            Indexer.getInstance().setMotorSpeedInPercent(0);
            Transfer.getInstance().setMotorSpeedInPercent(0);
        }
    }

}