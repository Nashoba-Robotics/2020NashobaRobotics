/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class DeltaAngleCommand extends NRCommand {

  Angle delta;

  public DeltaAngleCommand(Angle delta) {
    super(Turret.getInstance());
    this.delta = delta;
  }

  public void onStart() {
    Turret.getInstance().setAngle(delta.add(Turret.getInstance().getAngle()));
  }

  
  protected boolean isFinishedNR() {
    return true;
  }
}
