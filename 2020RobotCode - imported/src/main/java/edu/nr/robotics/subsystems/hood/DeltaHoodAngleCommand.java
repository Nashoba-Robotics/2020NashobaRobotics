package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class DeltaHoodAngleCommand extends NRCommand
{

    private Angle deltaAngle;

    public DeltaHoodAngleCommand(Angle deltaAngle)
    {
        super(Hood.getInstance());
        this.deltaAngle = deltaAngle;
    }

    public void onStart()
    {
        Hood.getInstance().setAngle(deltaAngle.add(Hood.getAngle()));
    }

    public boolean isFinishedNR()
    {
        return true;
    }
}