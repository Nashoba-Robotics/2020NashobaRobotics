package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class DeltaHoodAngleSmartDashboardCommand extends NRCommand {

    public DeltaHoodAngleSmartDashboardCommand(){
        super(Hood.getInstance());
    }

    public void onStart(){
        Hood.getInstance().setAngle(Hood.deltaAngleHood.add(Hood.getInstance().getAngle()));
    }

    public boolean isFinishedNR(){
        return true;
    }

}