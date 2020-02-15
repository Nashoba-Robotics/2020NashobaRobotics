package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class HoodUpButtonCommand extends NRCommand {

    public HoodUpButtonCommand(){
        super(Hood.getInstance());
    }

    public void onStart(){
        Hood.getInstance().setAngle(Hood.getInstance().getAngle().add(new Angle(5, Angle.Unit.DEGREE)));
    }

    public boolean isFinishedNR(){
        return true;
    }
}