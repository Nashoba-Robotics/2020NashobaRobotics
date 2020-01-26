package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Speed;

public class IndexerSetVelocityCommand extends NRCommand {

    private Speed target;

    public IndexerSetVelocityCommand(Speed goal){
        super(Indexer.getInstance());
        target = goal;

    }

    protected void onStart(){
        Indexer.getInstance().setSpeed(target);
    }

    public boolean isFinishedNR(){
        return true;
    }

}