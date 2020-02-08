package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;

public class IndexerSetVelocitySmartDashboardCommand extends NRCommand {

    public IndexerSetVelocitySmartDashboardCommand(){
        super(Indexer.getInstance());
    }

    @Override
    public void onStart(){
        Indexer.getInstance().setSpeed(Indexer.goalSpeed);
    //    System.out.println(Indexer.goalSpeed.get(Distance.Unit.INCH, Time.Unit.SECOND));
    }

    public boolean isFinishedNR(){
        return true;
    }

}