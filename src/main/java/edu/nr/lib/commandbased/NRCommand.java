package edu.nr.lib.commandbased;
 
import java.util.ArrayList;
 
import edu.wpi.first.wpilibj2.command.Command;
 
import edu.wpi.first.wpilibj2.command.CommandBase;
 
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
 
import edu.wpi.first.wpilibj.Timer;
 
/**
* A modified version of the WPILib Command class that provides additional
* lifecycle methods.
*
*/
public class NRCommand extends CommandBase {
 
    boolean forceCancel = false;

    public NRCommand(ArrayList<NRSubsystem> subsystems) {
        super();
        requires(subsystems);
    }
    
    public NRCommand(){
        super();
    }
    
    public NRCommand(NRSubsystem[] subsystems) {
        super();
        ArrayList<NRSubsystem> subsystemsArrList = new ArrayList<>();
        
 
        for (int i = 0; i < subsystems.length; i++) {
            subsystemsArrList.add(subsystems[i]);
        }
        requires(subsystemsArrList);
    }
 
    /**
    * Constructor used to set this commands visible name in SmartDashboard
    *
    * @param name
    */
    public NRCommand(ArrayList<NRSubsystem> subsystems, String name) {
        super();
        setName(name);
        requires(subsystems);
        
    }
 
    public NRCommand(ArrayList<NRSubsystem> subsystems, String name, double timeout) {
        //super(timeout);
        super();
        
        setName(name);
        requires(subsystems);
        
 
    }
 
    public NRCommand(ArrayList<NRSubsystem> subsystems, double timeout) {
        //super(timeout);
        super();
        
        requires(subsystems);
        
    }
    
    public NRCommand(NRSubsystem subsystem) {
        super();
        addRequirements(subsystem);
        
    }
 
    /**
    * Constructor used to set this commands visible name in SmartDashboard
    *
    * @param name
    */
    public NRCommand(NRSubsystem subsystem, String name) {
        super();
        setName(name);
        addRequirements(subsystem);
        
    }
 
    public NRCommand(NRSubsystem subsystem, String name, double timeout) {
        //super(timeout);
        super();
        
        setName(name);
        addRequirements(subsystem);
        
    }
 
    public NRCommand(NRSubsystem subsystem, double timeout) {
        //super(timeout);
        super();
        
        addRequirements(subsystem);
        
    }
 
    /**
    * Constructor used to set this commands visible name in SmartDashboard
    *
    * @param name
    */
    public NRCommand(String name) {
        super();
        setName(name);
        
    }
 
    public NRCommand(String name, double timeout) {
        //super(timeout);
        super();
        
        setName(name);
        
    }
 
    //Ethan has small arms
    public NRCommand(double timeout) {
        //super(timeout);
        
        
    }
    
    private void requires(ArrayList<NRSubsystem> subsystems) {
        for(NRSubsystem s : subsystems) {
            addRequirements(s);
        }
    }
 
    private boolean reset;
 
    /**
    * Called every time the command starts
    */
    protected void onStart() {}
 
    /**
    * Called every loop while the command is active
    */
    protected void onExecute() {}
 
    /**
    * Called when the command ends
    *
    * @param interrupted
    *            True if the command was interrupted
    */
    protected void onEnd(boolean interrupted) {onEnd();}
    
    protected void onEnd() {}
 
    @Override
    public void initialize() {
        onStart();
        reset = false;
    }
 
 
    @Override
    public final void execute() {
        if (reset) {
            onStart();
            reset = false;
        }
 
        onExecute();
    }
 
    
    protected void end() {
        reset = true;
        forceCancel = false;
        onEnd(false);
    }
 
    
    protected final void interrupted() {
        reset = true;
        forceCancel = false;
        onEnd(true);
    }
 
    @Override
    public final boolean isFinished() {
        return forceCancel || isFinishedNR();
    }
    
    protected boolean isFinishedNR() {return true;}
    
    protected final void makeFinish() {
        System.err.println(getName() + " was made to finish");
        //Needs editing
        forceCancel = true;
    }
    
    public static void cancelCommand(Command command) {
        
        if(command == null)
            return;
        
        System.err.println("Cancelling " + command.getName());
        /*
        
        if(command instanceof NRCommand)
            ((NRCommand) command).makeFinish();
        else {
            if(((NRCommand)command).getGroup() != false)
            {
                //Cancel all other commands in the group
            }
                
            else {
                command.cancel();
            }
<<<<<<< HEAD
        }
        */
=======
            
        }*/
>>>>>>> 5bb3b6bbb6181d2cd7f91d5e3be669e9127aa109
 
    }
    
}

