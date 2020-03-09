package edu.nr.robotics.multicommands;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.network.LimelightNetworkTable.Pipeline;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.turret.Turret;

public class AcquireTargetCommand extends NRCommand
{
    public AcquireTargetCommand()
    {
        super(new NRSubsystem[] {Hood.getInstance(), Turret.getInstance()});
    }

    @Override
    protected void onStart() 
    {
        LimelightNetworkTable.getInstance().setPipeline(Pipeline.Target);
        LimelightNetworkTable.getInstance().lightLED(true);
        //Turret.getInstance().setP(8);
    }

    @Override
    protected void onExecute()
    {
        if(!OI.getInstance().getManualMode())
        {

            if(LimelightNetworkTable.getInstance().getDistance().get(Distance.Unit.FOOT) >= 18)
            {
                Shooter.getInstance().setMotorSpeed(new AngularSpeed(6000, Angle.Unit.ROTATION, Time.Unit.MINUTE));
            }
            else{
                Shooter.getInstance().setMotorSpeed(Shooter.SHOOT_SPEED);
            }
            Turret.getInstance().setAngle((Turret.getInstance().getAngle().sub(LimelightNetworkTable.getInstance().getHorizOffset())));
            Hood.getInstance().setAngle(Hood.getInstance().getAngleLimelight());
            //System.out.println(LimelightNetworkTable.getInstance().getDistance().get(Distance.Unit.FOOT));
            //System.out.println(Hood.getInstance().getAngleLimelight().get(Angle.Unit.DEGREE));
        }
    }

    @Override
    protected boolean isFinishedNR()
    {
        if(OI.getInstance().getManualMode())
        {
            return true;
        }
        return false;
    }

    @Override
    protected void onEnd()
    {
        LimelightNetworkTable.getInstance().lightLED(false);
        //Shooter.getInstance().setNeutralOutput();
        //Hood.getInstance().setAngle(Angle.ZERO);
        //Turret.getInstance().setP(0.4);
    }
}