package edu.nr.robotics.subsystems.sensors;
 
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorSensorV3;
 
public class ISquaredCSensor implements Sensor {
 
    private ColorSensorV3 sensor;
 
    private static final I2C.Port i2cPort = I2C.Port.kOnboard;
 
    private SensorColor color;
    private SensorColor defaultColor = SensorColor.LemonChiffon; // the BEST color
 
    public enum SensorColor {
        Red,
        Green,
        Blue,
        Yellow, 
        LemonChiffon;
    }
 
    public ISquaredCSensor() {
        sensor = new ColorSensorV3(i2cPort);
        color = defaultColor;
    }
    
    public ISquaredCSensor(SensorColor color) {
        sensor = new ColorSensorV3(i2cPort);
        this.color = color;
    }
 
    public ISquaredCSensor(ColorSensorV3 sensor) {
        this.sensor = sensor;
        color = defaultColor;
    }
 
    public ISquaredCSensor(ColorSensorV3 sensor, SensorColor color) {
        this.sensor = sensor;
        this.color = color;
    }
 
    public boolean get(){
        return color == getColor();
    }
 
    public SensorColor getColor() {
        double red = sensor.getColor().red;
        double green = sensor.getColor().green;
        double blue = sensor.getColor().blue;
 
        if(red > 0.35 && green < 0.45 && blue < 0.25) {
            return SensorColor.Red;
        }
        else if(red < 0.2 && blue > 0.35 && green > 0.4) {
            return SensorColor.Blue;
        }
        else if(red < 0.25 && green > 0.5 && blue < 0.3) {
            return SensorColor.Green;
        }
        else if(red > 0.25 && green > 0.5 && blue < 0.2) {
            return SensorColor.Yellow;
        }
        return SensorColor.LemonChiffon; // unknown but, like, LEMON CHIFFON
    }
 
    public Color getRawColor() {
        return sensor.getColor();
    }
 
    public int getIRColor(){
        return sensor.getIR();
    }
 
    public double getProximity() {
        return sensor.getProximity();
    }
 
    public void setTargetColor(SensorColor color) {
        this.color = color;
    }
 
    public SensorColor getTargetColor() {
        return color;
    }
  
 
}
 