/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;

public class HoodJoystickCommand extends JoystickCommand {
  public HoodJoystickCommand() {
    super(Hood.getInstance());
  }



  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void onExecute() {
    if(!OI.climbMode)
      Hood.getInstance().setMotorSpeedRaw(OI.getInstance().getOperatorLeftTurn());
  }

  public long getPeriodOfCheckingForSwitchToJoystick() {
    return 100;
  }

  public boolean shouldSwitchToJoystick() {
    return OI.getInstance().isOperatorLeftNonZero();
  }

}
