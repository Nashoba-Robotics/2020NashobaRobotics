package edu.nr.robotics.subsystems.intake;

import edu.nr.lib.commandbased.NRCommand;

public class IntakeSubroutineCommand extends NRCommand {

    public IntakeSubroutineCommand(){
        super(Intake.getInstance());
    }

    public void onStart(){
        if(Intake.getInstance().isIntakeDeployed()){
            Intake.getInstance().disable();
            Intake.getInstance().retractIntake();
        }else{
            Intake.getInstance().deployIntake();;
            Intake.getInstance().setMotorSpeedRaw(Intake.INTAKE_PERCENT);
        }
    }

    public boolean isFinishedNR(){
        return true;
    }
}