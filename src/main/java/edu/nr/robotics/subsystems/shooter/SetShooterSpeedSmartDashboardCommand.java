package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;


public class SetShooterSpeedSmartDashboardCommand extends NRCommand {

    public SetShooterSpeedSmartDashboardCommand(){
        super(Shooter.getInstance());
    }

    protected void onExecute(){
        Shooter.getInstance().setMotorSpeed(Shooter.goalSpeed);
        //Shooter.getInstance().setMotorSpeedInPercent(1);
    }

    @Override
    protected boolean isFinishedNR(){
        return false;
    }

    @Override
    protected void onEnd()
    {
        Shooter.getInstance().setNeutralOutput();
    }

}