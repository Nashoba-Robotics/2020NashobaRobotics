package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class HoodDownButtonCommand extends NRCommand {

    public HoodDownButtonCommand(){
        super(Hood.getInstance());
    }

    public void onStart(){
        Hood.getInstance().setAngle(Hood.getAngle().sub(new Angle(-.5, Angle.Unit.DEGREE)));
    }

    public boolean isFinishedNR(){
        return true;
    }
}