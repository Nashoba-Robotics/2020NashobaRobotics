package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.gyro.GyroCorrection;
import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.units.Distance;


public class DriveForwardBasicCommand extends NRCommand {

    double percent;
    Distance distance;
    Distance initialPosition;
    GyroCorrection gyro;

    public DriveForwardBasicCommand(Distance distance, boolean b) {
            this(distance, Drive.ONE_D_PROFILE_DRIVE_PERCENT, b);
    }

    public DriveForwardBasicCommand(Distance distance, double percent, boolean b) {
        super(Drive.getInstance(), b);
        this.percent = percent;
        this.distance = distance;
      //  this.initialPosition = Drive.getInstance().getLeftPosition();
        gyro = new GyroCorrection();
    }

    public void onStart() {
        initialPosition = Drive.getInstance().getLeftPosition();
        gyro.reset();
    }

    public void onExecute() {
    //    Drive.getInstance().setMotorSpeedInPercent(percent * distance.signum(), percent * distance.signum(), 0);
    }

    public boolean isFinishedNR() {
        return (Drive.getInstance().getLeftPosition().sub(initialPosition)).abs().greaterThan(distance.abs());
    }

    public void onEnd(){
        Drive.getInstance().setMotorSpeedInPercent(0,0,0);
    }


}