package edu.nr.robotics.subsystems.colorwheel;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.nr.robotics.subsystems.sensors.ISquaredCSensor.SensorColor;

public class ColorWheelRotateCommand extends NRCommand{

    SensorColor oldColor;
    SensorColor currentColor;

    int colorCount = 0;

    public ColorWheelRotateCommand(){
        super(ColorWheel.getInstance());
    }

    public void onStart(){
        oldColor = EnabledSensors.colorSensor.getColor();
        currentColor = EnabledSensors.colorSensor.getColor();
    }

    public void onExecute(){
        oldColor = currentColor;
        currentColor = EnabledSensors.colorSensor.getColor();

        ColorWheel.getInstance().setMotorSpeedInPercent(1);

        if(oldColor != currentColor){
            colorCount ++;
        }
    }

    @Override
    public boolean isFinishedNR(){
        return colorCount >= 27; // three rotations is 24 color changes,
        // but use 27 in case we miss a couple, if command is reliable, we can cut it down
    }

    @Override
    public void onEnd(){
        ColorWheel.getInstance().setMotorSpeedInPercent(0);
    }

}