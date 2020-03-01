package edu.nr.robotics.multicommands;
 
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.indexer.IndexerDeltaPositionCommand;
import edu.nr.robotics.subsystems.indexer.IndexerDeltaPositionSmartDashboardCommand;
import edu.nr.robotics.subsystems.transfer.Transfer;
import edu.nr.robotics.subsystems.transfer.TransferCommand;
 
//OUTDATED. IndexingProcedureCommand and TransferProcedureCommand are most recent
public class CanWeIndexCommand extends NRCommand {
 
    boolean can = false;
 
    public CanWeIndexCommand() {
        super(new NRSubsystem[] { Indexer.getInstance(), Transfer.getInstance()});
    }
 
    public void onStart(){
        /*
        if(Transfer.getInstance().hasBall() && Indexer.getInstance().readyForBall()){
            can = true;
        }
        */

        //Needs to be changed such that indexer moves until sensors tell us we can transfer
 
        if(can){
            //Needs to be changed as delta position command is never called
            new TransferCommand(Transfer.TRANSFER_TIME);
            new IndexerDeltaPositionCommand(Indexer.SHOOT_ONE_DISTANCE);
        }
    }
 
    public boolean isFinishedNR(){
        return true;
    }
 
 
 
}
 
 
