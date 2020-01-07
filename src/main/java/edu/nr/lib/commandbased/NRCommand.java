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

	//Just FYI this doesn't work you need to copy paste more stuff from WPIOldCommands/.../Commands.java
	private double m_timeout = -1;
	//Just FYI this doesn't work you need to copy paste more stuff from WPIOldCommands/.../Commands.java
	private double m_startTime = -1;

	public NRCommand(ArrayList<NRSubsystem> subsystems) {
		super();
		requires(subsystems);
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
		setTimeout(timeout);
		setName(name);
		requires(subsystems);
	}

	public NRCommand(ArrayList<NRSubsystem> subsystems, double timeout) {
		//super(timeout);
		super();
		setTimeout(timeout);
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
		setTimeout(timeout);
		setName(name);
		addRequirements(subsystem);
	}

	public NRCommand(NRSubsystem subsystem, double timeout) {
		//super(timeout);
		super();
		setTimeout(timeout);
		addRequirements(subsystem);
	}
	

	
	public NRCommand() {
		super();
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
		setTimeout(timeout);
		setName(name);
	}
	//Ethan has small arms
	public NRCommand(double timeout) {
		//super(timeout);
		setTimeout(timeout);
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
		if(getGroup() == null) 
			cancel();
		forceCancel = true;
	}
	
	public static void cancelCommand(Command command) {
		
		if(command == null)
			return;
		
		System.err.println("Cancelling " + command.getName());

		
		if(command instanceof NRCommand)
			((NRCommand) command).makeFinish();
		else {
			if(command.getGroup() != null)
				cancelCommand(command.getGroup());
			else { 
				command.cancel();
			}
		}

	}
	
}
