package edu.nr.robotics.subsystems.turret;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ZeroTurretCommand extends CommandBase
{
    public ZeroTurretCommand()
    {
        addRequirements(Turret.getInstance());
    }

    @Override
    public void execute()
    {
        //Turret.getInstance().setAngle(new Angle(0, Angle.Unit.ROTATION));
        Turret.getInstance().setMotorSpeedInPercent(-0.5);
    }

    public boolean isFinishedNR()
    {
        return EnabledSensors.getInstance().LimTurretLeft.get();
    }
}