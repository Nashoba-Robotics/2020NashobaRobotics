package edu.nr.robotics.subsystems.colorwheel;

import edu.nr.lib.commandbased.NRCommand;

public class ToggleColorWheelSmartDashboardCommand extends NRCommand
{
    public ToggleColorWheelSmartDashboardCommand()
    {
        super(ColorWheel.getInstance());
    }

    @Override
    protected void onStart()
    {
        if(ColorWheel.getInstance().isColorWheelDeployed())
            ColorWheel.getInstance().deployColorWheel();
        else
            ColorWheel.getInstance().retractColorWheel();
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}