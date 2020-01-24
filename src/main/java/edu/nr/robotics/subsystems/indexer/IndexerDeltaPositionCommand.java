/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.nr.robotics.subsystems.indexer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;

public class IndexerDeltaPositionCommand extends NRCommand {

    Distance delta;

    public IndexerDeltaPositionCommand(Distance delta){
        super(Indexer.getInstance());
        this.delta = delta;
    }

    public void onStart(){
        Indexer.getInstance().setPosition(Indexer.getInstance().getPosition().add(delta));
    }

    public boolean isFinishedNR(){
        return true;
    }
}
