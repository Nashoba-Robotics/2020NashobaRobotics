package edu.nr.robotics.subsystems.colorwheel;

import edu.nr.lib.commandbased.NRCommand;
import edu.nr.robotics.GameData;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.nr.robotics.subsystems.sensors.ISquaredCSensor.SensorColor;


public class TargetColorCommand extends NRCommand{

    SensorColor currentColor;

    public TargetColorCommand(){
        super(ColorWheel.getInstance());
    }

    public void onStart(){
        EnabledSensors.colorSensor.setTargetColor(GameData.targetColor);
    }

    @Override
    protected void onExecute() {
        ColorWheel.getInstance().setMotorSpeedInPercent(.5);
    }

    @Override
    public void onEnd(){
        ColorWheel.getInstance().setMotorSpeedInPercent(0);
    }

    @Override
    public boolean isFinishedNR(){
        return EnabledSensors.colorSensor.get();
    }

} 