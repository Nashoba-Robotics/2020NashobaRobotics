package edu.nr.robotics.subsystems.transfer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.multicommands.States;
import edu.nr.robotics.multicommands.States.State;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.transfer.Transfer;

public class TransferProcedureCommand extends NRCommand {

    public TransferProcedureCommand() {
        super(Transfer.getInstance());
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onExecute() {
        if (EnabledSubsystems.TRANSFER_ENABLED) {
            if (States.getState() == States.State.IndexerStillIndexing) 
            {
                Transfer.getInstance().neutralOutput();
            } 
            
            else if (States.getState() == States.State.ReadyToTransfer) 
            {
                Transfer.getInstance().neutralOutput();
            } 
            
            else if (States.getState() == States.State.PreparingToTransfer) 
            {
                Transfer.getInstance().setMotorSpeedInPercent(Transfer.TRANSFER_PERCENT);
            } 
            
            else if (States.getState() == States.State.IndexerReadyToShoot) 
            {
                Transfer.getInstance().setMotorSpeedInPercent(0);
            }
        } 
        
        else 
        {
            Transfer.getInstance().neutralOutput();
        }
    }

    @Override
    protected boolean isFinishedNR() {
        return false;
    }
}