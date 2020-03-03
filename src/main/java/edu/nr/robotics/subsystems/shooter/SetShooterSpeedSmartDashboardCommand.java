package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Time;


public class SetShooterSpeedSmartDashboardCommand extends NRCommand {

    public SetShooterSpeedSmartDashboardCommand(){
        super(Shooter.getInstance());
    }

    protected void onStart(){
        Shooter.getInstance().setMotorSpeed(Shooter.SHOOT_SPEED);
        //Shooter.getInstance().setMotorSpeedInPercent(1);
    }

    protected boolean isFinishedNR(){
        return true;
    }

}