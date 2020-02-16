package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
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
    }

}