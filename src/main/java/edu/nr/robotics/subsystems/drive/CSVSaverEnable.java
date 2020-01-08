package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;

public class CSVSaverEnable extends NRCommand {

    //implicit constructor here, makes inGroup for NRCommandGroup false... in case this causes problems

    public void onStart() {
        CSVSaver.getInstance().enable();
    }

}