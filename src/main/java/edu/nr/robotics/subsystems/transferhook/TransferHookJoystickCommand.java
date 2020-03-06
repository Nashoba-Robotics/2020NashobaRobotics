package edu.nr.robotics.subsystems.transferhook;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;

public class TransferHookJoystickCommand extends JoystickCommand {
  public TransferHookJoystickCommand() 
  {
    super(TransferHook.getInstance());
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void onExecute() {
    //TransferHook.getInstance().setMotorSpeedRaw(OI.getInstance().getTransferHookTurn());
  }

  public long getPeriodOfCheckingForSwitchToJoystick() {
    return 100;
  }

  public boolean shouldSwitchToJoystick() {

    return OI.getInstance().isTurretNonZero();

  }

}
