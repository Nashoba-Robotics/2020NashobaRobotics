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
   protected void onStart() { // make sure these run simultaneously
    if(!Intake.getInstance().isIntakeDeployed()){
        Intake.getInstance().deployIntake();
    }

       Intake.getInstance().setMotorSpeedRaw(-1);
       Transfer.getInstance().setMotorSpeedInPercent(-1);
       Indexer.getInstance().setMotorSpeedInPercent(-1);
       Shooter.getInstance().setMotorSpeedInPercent(-.6);
    
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
 
