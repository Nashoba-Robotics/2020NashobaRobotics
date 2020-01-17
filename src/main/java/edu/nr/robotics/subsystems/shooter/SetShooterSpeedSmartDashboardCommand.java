package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;


public class SetShooterSpeedSmartDashboardCommand extends NRCommand {

    public SetShooterSpeedSmartDashboardCommand(){
        super(Shooter.getInstance());
    }

    protected void onStart(){
        Shooter.getInstance().setMotorSpeed(Shooter.goalSpeed);
    }

    protected boolean isFinishedNR(){
        return true;
    }

}