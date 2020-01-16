package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class DeltaTurretAngleSmartDashboardCommand extends NRCommand {

    public DeltaTurretAngleSmartDashboardCommand(){
        super(Turret.getInstance());
    }

    public void onStart(){
        Turret.getInstance().setAngle(Turret.deltaAngle.add(Turret.getInstance().getAngle()));
    }

    public boolean isFinishedNR(){
        return true;
    }

}