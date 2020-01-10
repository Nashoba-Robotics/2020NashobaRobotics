package edu.nr.robotics.subsystems.sensors;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorSensorV3;

public class ISquaredCSensor implements Sensor {

    private ColorSensorV3 sensor;

    private static final I2C.Port i2cPort = I2C.Port.kOnboard;

    private Color color;
    private Color defaultColor = Color.kLemonChiffon; // the BEST color

    public ISquaredCSensor() {
        sensor = new ColorSensorV3(i2cPort);
        color = defaultColor;
    }
    
    public ISquaredCSensor(Color color) {
        sensor = new ColorSensorV3(i2cPort);
        this.color = color;
    }

    public ISquaredCSensor(ColorSensorV3 sensor) {
        this.sensor = sensor;
        color = defaultColor;
    }

    public ISquaredCSensor(ColorSensorV3 sensor, Color color) {
        this.sensor = sensor;
        this.color = color;
    }

    public boolean get(){
        return color == sensor.getColor();
    }

    public Color getColor() {
        return sensor.getColor();
    }

    public int getIRColor(){
        return sensor.getIR();
    }

    public double getProximity() {
        return sensor.getProximity();
    }
  

}


