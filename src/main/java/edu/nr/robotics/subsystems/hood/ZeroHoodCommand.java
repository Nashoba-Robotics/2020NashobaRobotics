package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ZeroHoodCommand extends CommandBase{
    public ZeroHoodCommand()
    {
        addRequirements(Hood.getInstance());
    }

    @Override
    public void execute()
    {
        Hood.getInstance().setMotorSpeedRaw(-0.4);
        
    }

    @Override
    public boolean isFinished()
    {
        return EnabledSensors.getInstance().LimHoodLower.get();
    }
}