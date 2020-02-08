package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Time;
import edu.wpi.first.wpilibj.Timer;

public class IndexerPukeCommand extends NRCommand{
    double start = 0;
    double currentTime = 0;
    
    public IndexerPukeCommand(){
        super(Indexer.getInstance());
    }

    public void onStart(){
        Indexer.getInstance().setMotorSpeedInPercent(Indexer.PUKE_PERCENT);
        start = Timer.getFPGATimestamp();
    }

    public void onExecute(){
        currentTime = Timer.getFPGATimestamp();
    }

    public void onEnd(){
        Indexer.getInstance().setMotorSpeedInPercent(0);
    }

    public boolean isFinishedNR(){
        return (currentTime - start > Indexer.PUKE_TIME.get(Time.Unit.SECOND));
    }

}