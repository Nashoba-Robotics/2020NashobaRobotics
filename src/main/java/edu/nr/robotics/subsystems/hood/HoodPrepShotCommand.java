package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.Equation;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.network.LimelightNetworkTable;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Distance;

public class HoodPrepShotCommand extends NRCommand
{
    //this ones for constantly adjusting
    
    private Distance limeLightDistance;
    private Equation hoodAngle = new Equation(){
    //change equation
        @Override
        public double getValue(double x) {
            return 0;
        }
    };
    
    public HoodPrepShotCommand()
    {
        super(Hood.getInstance());
    }

    @Override
    protected void onExecute()
    {
        this.limeLightDistance = LimelightNetworkTable.getInstance().getDistance();
        Hood.getInstance().setAngle( new Angle (hoodAngle.getValue(limeLightDistance.get(Distance.Unit.INCH)), Angle.Unit.DEGREE));
    }

    @Override
    protected boolean isFinishedNR()
    {
        return false;
    }
}