package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;

public class CSVSaverDisable extends NRCommand {

    public CSVSaverDisable(boolean b) {
        super(b);
    }

    public void onStart() {
        CSVSaver.getInstance().disable();
    }

}