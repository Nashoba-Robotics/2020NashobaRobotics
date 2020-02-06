package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;

public class IndexerSetVelocitySmartDashboardCommand extends NRCommand {

    public IndexerSetVelocitySmartDashboardCommand(){
        super(Indexer.getInstance());
    }

    @Override
    public void onStart(){
        Indexer.getInstance().setSpeed(Indexer.goalSpeed);
    }

    public boolean isFinishedNR(){
        return true;
    }

}