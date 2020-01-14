package edu.nr.robotics.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.nr.lib.commandbased.NRSubsystem;

public class Shooter extends NRSubsystem
{
    private static Shooter singleton;

    private Shooter()
    {

    }

    public synchronized static void init()
    {
        if(singleton == null)
            singleton = new Shooter();
    }

    public static Shooter getInstance()
    {
        if(singleton == null)
            init();
        return singleton;
    }

    public void disable()
    {

    }
    public void smartDashboardInfo()
    {

    }
}