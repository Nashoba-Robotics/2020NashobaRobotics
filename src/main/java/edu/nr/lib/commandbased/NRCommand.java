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
    boolean inGroup = false;
    private NRCommandGroup parent;
 
    //Just FYI this doesn't work you need to copy paste more stuff from WPIOldCommands/.../Commands.java
    private double m_timeout = -1;
    //Just FYI this doesn't work you need to copy paste more stuff from WPIOldCommands/.../Commands.java
    private double m_startTime = -1;
 
    public NRCommand(ArrayList<NRSubsystem> subsystems, boolean b) {
        super();
        requires(subsystems);
        this.inGroup = b;
    }
    
    public NRCommand(){
        super();
        this.inGroup = false;
    }
    
    public NRCommand(NRSubsystem[] subsystems, boolean b) {
        super();
        ArrayList<NRSubsystem> subsystemsArrList = new ArrayList<>();
        this.inGroup = b;
 
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
    public NRCommand(ArrayList<NRSubsystem> subsystems, String name, boolean b) {
        super();
        setName(name);
        requires(subsystems);
        this.inGroup = b;
    }
 
    public NRCommand(ArrayList<NRSubsystem> subsystems, String name, double timeout, boolean b) {
        //super(timeout);
        super();
        setTimeout(timeout);
        setName(name);
        requires(subsystems);
        this.inGroup = b;
 
    }
 
    public NRCommand(ArrayList<NRSubsystem> subsystems, double timeout, boolean b) {
        //super(timeout);
        super();
        setTimeout(timeout);
        requires(subsystems);
        this.inGroup = b;
    }
    
    public NRCommand(NRSubsystem subsystem, boolean b) {
        super();
        addRequirements(subsystem);
        this.inGroup = b;
    }
 
    /**
    * Constructor used to set this commands visible name in SmartDashboard
    *
    * @param name
    */
    public NRCommand(NRSubsystem subsystem, String name, boolean b) {
        super();
        setName(name);
        addRequirements(subsystem);
        this.inGroup = b;
    }
 
    public NRCommand(NRSubsystem subsystem, String name, double timeout, boolean b) {
        //super(timeout);
        super();
        setTimeout(timeout);
        setName(name);
        addRequirements(subsystem);
        this.inGroup = b;
    }
 
    public NRCommand(NRSubsystem subsystem, double timeout, boolean b) {
        //super(timeout);
        super();
        setTimeout(timeout);
        addRequirements(subsystem);
        this.inGroup = b;
    }
    
 
    
    public NRCommand(boolean b) {
        super();
        this.inGroup = b;
    }
 
    /**
    * Constructor used to set this commands visible name in SmartDashboard
    *
    * @param name
    */
    public NRCommand(String name, boolean b) {
        super();
        setName(name);
        this.inGroup = b;
    }
 
    public NRCommand(String name, double timeout, boolean b) {
        //super(timeout);
        super();
        setTimeout(timeout);
        setName(name);
        this.inGroup = b;
    }
 
    //Ethan has small arms
    public NRCommand(double timeout, boolean b) {
        //super(timeout);
        setTimeout(timeout);
        this.inGroup = b;
    }
    
    private void requires(ArrayList<NRSubsystem> subsystems) {
        for(NRSubsystem s : subsystems) {
            addRequirements(s);
        }
    }
    
    public boolean getGroup()
    {
        return inGroup;
    }
 
    public NRCommandGroup getParent()
    {
        return parent;
    }
 
    public void addParent(NRCommandGroup parent)
    {
        this.parent = parent;
        this.inGroup = true;
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
 
    //Just FYI this doesn't work you need to copy paste more stuff from WPIOldCommands/.../Commands.java
    protected final synchronized void setTimeout(double seconds) {
        if (seconds < 0) {
         throw new IllegalArgumentException("Seconds must be positive.  Given:" + seconds);
        }
        m_timeout = seconds;
     }
    
    //Just FYI this doesn't work you need to copy paste more stuff from WPIOldCommands/.../Commands.java
    private void startTiming() {
        m_startTime = Timer.getFPGATimestamp();
     }
 
    //Just FYI this doesn't work you need to copy paste more stuff from WPIOldCommands/.../Commands.java
    public final synchronized double timeSinceInitialized() {
        return m_startTime < 0 ? 0 : Timer.getFPGATimestamp() - m_startTime;
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
        if(getGroup() == false)
            cancel();
        forceCancel = true;
    }
    
    public static void cancelCommand(Command command, NRCommandGroup parent) {
        
        if(command == null)
            return;
        
        System.err.println("Cancelling " + command.getName());
 
        
        if(command instanceof NRCommand)
            ((NRCommand) command).makeFinish();
        else {
            if(((NRCommand)command).getGroup() != false)
            {
                //Cancel all other commands in the group
                parent.cancelCommandGroup();
            }
                
            else {
                command.cancel();
            }
        }
 
    }
    
}

