package edu.nr.robotics.subsystems.sensors;

import com.revrobotics.ColorSensorV3;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.indexer.Indexer;
import edu.nr.robotics.subsystems.transfer.Transfer;
import edu.wpi.first.wpilibj.I2C;

public class EnabledSensors {
    
    public static boolean limelightEnabled = false;

    private static EnabledSensors singleton;

    public AnalogSensor indexerPukeSensor;
    public AnalogSensor indexerSetting2;
    public AnalogSensor indexerSetting1;
    public AnalogSensor indexerSetting3;
    public AnalogSensor indexerShooterSensor;

    public AnalogSensor transferSensor;
    
    public DigitalSensor LimTurretLeft;
    public DigitalSensor LimTurretRight;

    public DigitalSensor LimHoodLower;

    public ISquaredCSensor colorSensor;

    private EnabledSensors(){
        if(EnabledSubsystems.INDEXER_ENABLED){
            //this.indexerPukeSensor = new AnalogSensor(RobotMap.INDEXER_PUKE_SENSOR, Indexer.INDEXER_PUKE_SENSOR_THRESHOLD);
            this.indexerSetting2 = new AnalogSensor(RobotMap.INDEXER_SETTING2, Indexer.INDEXER_SETTING1_THRESHOLD);
            this.indexerSetting1 = new AnalogSensor(RobotMap.INDEXER_SETTING1, Indexer.INDEXER_SETTING2_THRESHOLD);
            this.indexerSetting3 = new AnalogSensor(RobotMap.INDEXER_SETTING3, Indexer.INDEXER_SETTING3_THRESHOLD);
            this.indexerShooterSensor = new AnalogSensor(RobotMap.INDEXER_SHOOTER_SENSOR, Indexer.INDEXER_SHOOTER_SENSOR_THRESHOLD);            
        }

        if(EnabledSubsystems.TRANSFER_ENABLED){
            transferSensor = new AnalogSensor(RobotMap.TRANSFER_SENSOR, Transfer.TRANSFER_THRESHOLD);
        }

        if(EnabledSubsystems.TURRET_ENABLED){
            LimTurretLeft = new DigitalSensor(RobotMap.LIM_TURRET_LEFT);
            LimTurretRight = new DigitalSensor(RobotMap.LIM_TURRET_RIGHT); 
        }

        if(EnabledSubsystems.HOOD_ENABLED){
            LimHoodLower = new DigitalSensor(RobotMap.LIM_HOOD_LOWER);
        }

        if(EnabledSubsystems.COLOR_WHEEL_ENABLED){
            colorSensor = new ISquaredCSensor(new ColorSensorV3(I2C.Port.kOnboard));
        }
    }

    public static EnabledSensors getInstance(){
    if(singleton == null){
        singleton = new EnabledSensors();
    }
    return singleton;
    }


    //more sensors, way more sensors

    //public static DigitalInput cargoIntakeSensorOne = new DigitalInput(RobotMap.CARGO_INTAKE_SENSOR_PORT_1);
    //public static DigitalInput cargoIntakeSensorTwo = new DigitalInput(RobotMap.CARGO_INTAKE_SENSOR_PORT_2);
    //public static DigitalInput cargoIntakeSensorThree = new DigitalInput(RobotMap.CARGO_INTAKE_SENSOR_PORT_3);

 
    //public static DigitalSensor forceSensorOne = new DigitalSensor(RobotMap.FORCE_SENSOR_1);
    //public static DigitalSensor forceSensorTwo = new DigitalSensor(RobotMap.FORCE_SENSOR_2);
    //public static DigitalSensor forceSensorThree = new DigitalSensor(RobotMap.FORCE_SENSOR_3);

}
