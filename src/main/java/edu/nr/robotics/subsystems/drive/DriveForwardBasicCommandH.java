
package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.lib.gyro.GyroCorrection;
import edu.nr.lib.units.Distance;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveForwardBasicCommandH extends NRCommand {

	double driveSpeedPercent;
	Distance distance;
	Distance initialPosition;
	GyroCorrection gyro;

	public DriveForwardBasicCommandH(Distance distance) {
		this(distance, Drive.ONE_D_PROFILE_DRIVE_PERCENT);
	}

	public DriveForwardBasicCommandH(Distance distance, double percent) {
		super(Drive.getInstance());
		this.driveSpeedPercent = percent;
		this.distance = distance;
		gyro = new GyroCorrection();
	}

	public void onStart() {
		initialPosition = Drive.getInstance().getHPosition();
		gyro.reset();
    }

	public void onExecute() {
		double turnValue = gyro.getTurnValue(Drive.kP_thetaOneD, false);
		Drive.getInstance().setMotorSpeedInPercent(-turnValue, turnValue, driveSpeedPercent * distance.signum());
	}

	public void onEnd() {
		Drive.getInstance().setMotorSpeedInPercent(0, 0, 0);;
	}

	protected boolean isFinishedNR() {
		return (Drive.getInstance().getHPosition().sub(initialPosition)).abs().greaterThan(distance.abs());	
	}
}