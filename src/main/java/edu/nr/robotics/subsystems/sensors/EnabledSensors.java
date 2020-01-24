package edu.nr.robotics.subsystems.sensors;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.indexer.Indexer;

public class EnabledSensors {
    
    public static boolean limelightEnabled = false;

    public static AnalogSensor IndexerInput = new AnalogSensor(RobotMap.INDEXER_INPUT, Indexer.INDEXER_INPUT_THRESHOLD);
    public static AnalogSensor IndexerSpacingClose = new AnalogSensor(RobotMap.INDEXER_SPACING_CLOSE, Indexer.INDEXER_SPACING_CLOSE_THRESHOLD);
    public static AnalogSensor IndexerSpacingFar = new AnalogSensor(RobotMap.INDEXER_SPACING_FAR, Indexer.INDEXER_SPACING_FAR_THRESHOLD);
    public static AnalogSensor IndexerReadyShot = new AnalogSensor(RobotMap.INDEXER_READY_SHOT, Indexer.INDEXER_READY_SHOT_THRESHOLD);




    //more sensors, way more sensors

    //public static DigitalInput cargoIntakeSensorOne = new DigitalInput(RobotMap.CARGO_INTAKE_SENSOR_PORT_1);
    //public static DigitalInput cargoIntakeSensorTwo = new DigitalInput(RobotMap.CARGO_INTAKE_SENSOR_PORT_2);
    //public static DigitalInput cargoIntakeSensorThree = new DigitalInput(RobotMap.CARGO_INTAKE_SENSOR_PORT_3);

 
    //public static DigitalSensor forceSensorOne = new DigitalSensor(RobotMap.FORCE_SENSOR_1);
    //public static DigitalSensor forceSensorTwo = new DigitalSensor(RobotMap.FORCE_SENSOR_2);
    //public static DigitalSensor forceSensorThree = new DigitalSensor(RobotMap.FORCE_SENSOR_3);

}

