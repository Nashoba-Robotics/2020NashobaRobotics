/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.JoystickCommand;
import edu.nr.robotics.OI;

public class TurretJoystickCommand extends JoystickCommand 
{
  public TurretJoystickCommand() 
  {
    super(Turret.getInstance());
  }

  @Override
  protected void onExecute() 
  {
    Turret.getInstance().setMotorSpeedInPercent(OI.getInstance().getTurretTurn());
   // System.out.println("turret joystick value" + OI.getInstance().getTurretTurn());
  }

  public long getPeriodOfCheckingForSwitchToJoystick() 
  {
    return 100;
  }

  public boolean shouldSwitchToJoystick() {
    return OI.getInstance().isTurretNonZero();
  }

}
