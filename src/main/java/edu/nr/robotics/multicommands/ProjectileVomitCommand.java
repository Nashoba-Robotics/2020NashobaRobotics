package edu.nr.robotics.multicommands;
 
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.indexer.IndexerPukeCommand;
import edu.nr.robotics.subsystems.intake.Intake;
import edu.nr.robotics.subsystems.intake.IntakePukeCommand;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.shooter.ShooterPukeCommand;
import edu.nr.robotics.subsystems.transfer.Transfer;
import edu.nr.robotics.subsystems.transfer.TransferPukeCommand;
 
public class ProjectileVomitCommand extends NRCommand {
 
   public ProjectileVomitCommand(){
       super(new NRSubsystem [] {Intake.getInstance(), Transfer.getInstance(), Indexer.getInstance(), Shooter.getInstance()});
   }
 
    @Override
    protected void onStart() 
    {
        if(!Intake.getInstance().isIntakeDeployed()){
            Intake.getInstance().deployIntake();

        //Might need wait command for 100 ms such that intake is fully deployed before puking
    }

    }
    @Override
    protected void onExecute() {
        Intake.getInstance().setMotorSpeedRaw(Intake.PUKE_PERCENT);
        Transfer.getInstance().setMotorSpeedInPercent(Transfer.PUKE_PERCENT);
        Indexer.getInstance().setMotorSpeedInPercent(Indexer.PUKE_PERCENT);
        Shooter.getInstance().setMotorSpeedInPercent(Shooter.PUKE_PERCENT);
    
    }

    @Override
    protected void onEnd() {
     Intake.getInstance().setMotorSpeedRaw(0);
     Transfer.getInstance().setMotorSpeedInPercent(0);
     Indexer.getInstance().setMotorSpeedInPercent(0);
     Shooter.getInstance().setMotorSpeedInPercent(0);
    }
 
   @Override
   protected boolean isFinishedNR(){
       return true;
   }
}
 
 
 
 
