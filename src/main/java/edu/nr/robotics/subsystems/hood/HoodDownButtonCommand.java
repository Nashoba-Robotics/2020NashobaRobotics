package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class HoodDownButtonCommand extends NRCommand {

    public HoodDownButtonCommand(){
        super(Hood.getInstance());
    }

    @Override
    public void onStart(){
        Hood.getInstance().setMotorSpeedRaw(-0.1);
    }

    @Override
    public boolean isFinishedNR(){
        return false;
    }

    @Override
    protected void onEnd() {
        Hood.getInstance().setMotorSpeedRaw(0);
    }

    
}