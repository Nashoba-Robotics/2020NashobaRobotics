package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;

public class ZeroHoodCommand extends NRCommand{


    public ZeroHoodCommand(){
        super(Hood.getInstance());
    }

    public void onStart(){
        Hood.getInstance().setMotorSpeedRaw(-.4);
    }

    public boolean isFinishedNR(){
        return true;
    }
}