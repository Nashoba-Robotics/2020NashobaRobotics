package edu.nr.lib.commandbased;
 
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
 
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
 
/**
 * Every subsystem that extends NRSubsystem should have a singleton.
 * 
 * The static function to initialize the singleton should, after initializing the singleton, call setJoystickCommand().
 * 
 * initDefaultCommand() and setDefaultCommand() should never be called.
 */
public abstract class NRSubsystem extends SubsystemBase implements SmartDashboardSource, Periodic {
 
    public static ArrayList<NRSubsystem> subsystems = new ArrayList<>();
 
    public abstract void disable();
    
    JoystickCommand joystickCommand;
    
    Timer switchToJoystickTimer;
    
 
 
    public NRSubsystem() {
        super();
        NRSubsystem.subsystems.add(this);
        periodics.add(this);
        sources.add(this);
    }
    
 
 
    public void periodic()
    {
        
    }
}
 

