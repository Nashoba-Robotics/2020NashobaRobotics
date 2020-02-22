package edu.nr.robotics.subsystems.bashbar;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BashBar extends NRSubsystem
{
    private static BashBar singleton;

    private Solenoid BashBarSolenoid;

    private BashBar()
    {
        BashBarSolenoid = new Solenoid(RobotMap.PCM_ID, RobotMap.BASH_BAR_SOLENOID);
    }

    public synchronized static void init()
    {
        if(singleton == null)
            singleton = new BashBar();
    }

    public static BashBar getInstance()
    {
        if(singleton == null)
        {
            init();
        }
        return singleton;
    }

    public void disable()
    {
        retractBashBar();
    }

    public void SmartDashboardInit()
    {
        SmartDashboard.putBoolean("Bash Bar Deployed", isBashBarDeployed());
    }

    public void smartDashboardInfo()
    {
        SmartDashboard.putBoolean("Bash Bar Deployed", isBashBarDeployed());
    }

    public enum State {
		DEPLOYED, RETRACTED;
		
		private static boolean DEPLOYED_VALUE = true;
		private static boolean RETRACTED_VALUE = false;
		
		private static State getDeployState(boolean val) {
			if(val == State.DEPLOYED_VALUE) {
				return State.DEPLOYED;
			} else {
				return State.RETRACTED;
			}
		}
    }

    public State currentDeployState()
    {
        if(BashBarSolenoid != null)
        {
            return State.getDeployState(BashBarSolenoid.get());
        }
        return State.RETRACTED;
    }

    public void deployBashBar()
    {
        if(BashBarSolenoid != null)
        {
            BashBarSolenoid.set(State.DEPLOYED_VALUE);
        }
    }

    public void retractBashBar()
    {
        if(BashBarSolenoid != null)
        {
            BashBarSolenoid.set(State.RETRACTED_VALUE);
        }
    }

    public boolean isBashBarDeployed()
    {
        return currentDeployState() == State.DEPLOYED;
    }
}