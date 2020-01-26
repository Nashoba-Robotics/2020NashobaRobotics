package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;

public class IndexerDeltaPositionSmartDashboardCommand extends NRCommand {


    public IndexerDeltaPositionSmartDashboardCommand(){
        super(Indexer.getInstance());
    }

    public void onStart(){
        Indexer.getInstance().setPosition(Indexer.DeltaPosition.add(Indexer.getInstance().getPosition()));
    }

    public boolean isFinishedNR(){
        return true;
    }
}