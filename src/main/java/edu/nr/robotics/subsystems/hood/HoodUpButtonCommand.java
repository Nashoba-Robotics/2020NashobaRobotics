package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class HoodUpButtonCommand extends NRCommand {

    public HoodUpButtonCommand(){
        super(Hood.getInstance());
    }

    @Override
    public void onExecute(){
        Hood.getInstance().setMotorSpeedRaw(0.1);
    }

    @Override
    public boolean isFinishedNR(){
        return false;
    }
    
    @Override
    public void onEnd(){
        Hood.getInstance().setMotorSpeedRaw(0);
    }

    
}