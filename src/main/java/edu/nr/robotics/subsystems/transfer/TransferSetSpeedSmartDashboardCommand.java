package edu.nr.robotics.subsystems.transfer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.robotics.subsystems.turret.Turret;
 
public class TransferSetSpeedSmartDashboardCommand extends NRCommand{
 
    public TransferSetSpeedSmartDashboardCommand(){
        super(Transfer.getInstance());
    }
 
    public void onStart(){
        Transfer.getInstance().setMotorSpeedInPercent(Transfer.goalSpeed);
    }
 
    protected boolean isFinishedNR(){
        return true;
    }
 
}