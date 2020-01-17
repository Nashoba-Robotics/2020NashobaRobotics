

package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.AngularSpeed;

public class SetShooterSpeedCommand extends NRCommand{

    private AngularSpeed targetSpeed;

    public SetShooterSpeedCommand(AngularSpeed target){
        super(Shooter.getInstance());
        targetSpeed = target;
    }

    protected void onStart(){
        Shooter.getInstance().setMotorSpeed(targetSpeed);
    }

    protected boolean isFinishedNR(){
        return true;
    }


}
