package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;
import edu.wpi.first.wpilibj.Timer;

public class IntakeSubroutineCommand extends NRCommand {

    double start = 0;
    double current = 0;

    public IntakeSubroutineCommand(){
        super(Intake.getInstance());
    }

    @Override
    protected void onStart()
    {
        start = Timer.getFPGATimestamp();

        if(Intake.getInstance().isIntakeDeployed()){
            Intake.getInstance().setMotorSpeedRaw(0);
            Intake.getInstance().retractIntake();
        }else{
            Intake.getInstance().deployIntake();          
        }
    }

    @Override
    protected void onExecute(){
        current = Timer.getFPGATimestamp();
        if(current - start > 0.1 && Intake.getInstance().isIntakeDeployed()){
            Intake.getInstance().setMotorSpeedRaw(Intake.INTAKE_PERCENT);
        }
    }

    @Override
    protected boolean isFinishedNR(){
        return current - start > 0.1 || !Intake.getInstance().isIntakeDeployed();
    }
}
