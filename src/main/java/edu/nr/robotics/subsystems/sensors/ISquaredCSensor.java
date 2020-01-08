package edu.nr.robotics.subsystems.sensors;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorSensorV3;

public class ISquaredCSensor implements Sensor {

    private final I2C.Port i2cPort = I2C.Port.kOnboard;

    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
   
    
        Color detectedColor = m_colorSensor.getColor();

   
        double IR = m_colorSensor.getIR();

    
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("IR", IR);

        int proximity = m_colorSensor.getProximity();
        

        SmartDashboard.putNumber("Proximity", proximity);

        public boolean get(){
            return false;
        }
  


}


