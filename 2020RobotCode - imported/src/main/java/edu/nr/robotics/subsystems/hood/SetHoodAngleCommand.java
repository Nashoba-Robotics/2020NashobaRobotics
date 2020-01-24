package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;

public class SetHoodAngleCommand extends NRCommand
{

    private Angle targetAngle;

    public SetHoodAngleCommand(Angle setToAngle)
    {
        super(Hood.getInstance());
        targetAngle = setToAngle;
    }

    @Override
    public void onStart()
    {
        Hood.getInstance().setAngle(targetAngle);
    }

    @Override
    public boolean isFinishedNR()
    {
        return true;
    }
}