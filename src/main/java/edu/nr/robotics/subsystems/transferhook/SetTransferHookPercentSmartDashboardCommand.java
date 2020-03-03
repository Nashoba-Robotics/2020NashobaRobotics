package edu.nr.robotics.subsystems.transferhook;

import edu.nr.lib.commandbased.NRCommand;

public class SetTransferHookPercentSmartDashboardCommand extends NRCommand
{
    public SetTransferHookPercentSmartDashboardCommand()
    {
        super(TransferHook.getInstance());
    }

    @Override
    protected void onStart()
    {
        TransferHook.getInstance().setMotorSpeedRaw(TransferHook.goalPercent);
    }

    @Override
    protected boolean isFinishedNR()
    {
        return true;
    }
}