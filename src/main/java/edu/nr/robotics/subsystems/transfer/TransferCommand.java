package edu.nr.robotics.subsystems.transfer;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Time;
import edu.wpi.first.wpilibj.Timer;

public class TransferCommand extends NRCommand {

    Time transferTime;
    double currentTime = 0;
    double start = 0;

    public TransferCommand(Time transferTime){
        super(Transfer.getInstance());
        this.transferTime = transferTime;        
    }

    public void onStart(){
        this.start = Timer.getFPGATimestamp();
        Transfer.getInstance().setMotorSpeedInPercent(Transfer.TRANSFER_PERCENT);
    }
    
    @Override
    public void end(boolean interrupted) {
        Transfer.getInstance().setMotorSpeedInPercent(0);
    }

    @Override
    public boolean isFinishedNR(){
        currentTime = Timer.getFPGATimestamp();
        System.out.println(currentTime - start);
        return (currentTime - start) > transferTime.get(Time.Unit.SECOND);
    }
}