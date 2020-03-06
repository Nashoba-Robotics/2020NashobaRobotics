package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;

public class EnableSniperMode extends NRCommand {

	private boolean bool;
	
	public static final double TURN_JOYSTICK_MULTIPLIER_LOW = 0.5;
	public static final double MOVE_JOYSTICK_MULTIPLIER_LOW = 0.6;
	
	public EnableSniperMode() {
		
	}
	
	@Override
	protected void onExecute() {
		
			Drive.TURN_JOYSTICK_MULTIPLIER = TURN_JOYSTICK_MULTIPLIER_LOW;
			Drive.MOVE_JOYSTICK_MULTIPLIER = MOVE_JOYSTICK_MULTIPLIER_LOW;
				
	}

	@Override
	protected void onEnd(){
		Drive.TURN_JOYSTICK_MULTIPLIER = .7;
		Drive.MOVE_JOYSTICK_MULTIPLIER = 1;
	}

	@Override
	protected boolean isFinishedNR(){
		return false;
	}
}