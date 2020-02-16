package edu.nr.robotics.subsystems.sensors;

import com.revrobotics.ColorSensorV3;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.transfer.Transfer;
import edu.wpi.first.wpilibj.I2C;

public class EnabledSensors {
    
    public static boolean limelightEnabled = false;

    public static AnalogSensor indexerPukeSensor = new AnalogSensor(RobotMap.INDEXER_PUKE_SENSOR, Indexer.INDEXER_PUKE_SENSOR_THRESHOLD);
    public static AnalogSensor indexerSetting2 = new AnalogSensor(RobotMap.INDEXER_SETTING2, Indexer.INDEXER_SETTING1_THRESHOLD);
    public static AnalogSensor indexerSetting1 = new AnalogSensor(RobotMap.INDEXER_SETTING1, Indexer.INDEXER_SETTING2_THRESHOLD);
    public static AnalogSensor indexerSetting3 = new AnalogSensor(RobotMap.INDEXER_SETTING3, Indexer.INDEXER_SETTING3_THRESHOLD);
    public static AnalogSensor indexerShooterSensor = new AnalogSensor(RobotMap.INDEXER_SHOOTER_SENSOR, Indexer.INDEXER_SHOOTER_SENSOR_THRESHOLD);
    public static AnalogSensor transferSensor = new AnalogSensor(RobotMap.TRANSFER_SENSOR, Transfer.TRANSFER_THRESHOLD);
    
    public static DigitalSensor LimTurretLeft = new DigitalSensor(RobotMap.LIM_TURRET_LEFT);
    public static DigitalSensor LimTurretRight = new DigitalSensor(RobotMap.LIM_TURRET_RIGHT);

    public static DigitalSensor LimHoodLower = new DigitalSensor(RobotMap.LIM_HOOD_LOWER);

    public static ISquaredCSensor colorSensor = new ISquaredCSensor(new ColorSensorV3(I2C.Port.kOnboard));



    //more sensors, way more sensors

    //public static DigitalInput cargoIntakeSensorOne = new DigitalInput(RobotMap.CARGO_INTAKE_SENSOR_PORT_1);
    //public static DigitalInput cargoIntakeSensorTwo = new DigitalInput(RobotMap.CARGO_INTAKE_SENSOR_PORT_2);
    //public static DigitalInput cargoIntakeSensorThree = new DigitalInput(RobotMap.CARGO_INTAKE_SENSOR_PORT_3);

 
    //public static DigitalSensor forceSensorOne = new DigitalSensor(RobotMap.FORCE_SENSOR_1);
    //public static DigitalSensor forceSensorTwo = new DigitalSensor(RobotMap.FORCE_SENSOR_2);
    //public static DigitalSensor forceSensorThree = new DigitalSensor(RobotMap.FORCE_SENSOR_3);

}

