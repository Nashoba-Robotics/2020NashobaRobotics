package edu.nr.robotics.subsystems.colorwheel;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorSensorV3;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Time;
import edu.nr.robotics.GameData;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jdk.jfr.Enabled;

public class ColorWheel extends NRSubsystem{

    private static ColorWheel singleton; 

    private VictorSPX colorWheelVictor;

    public static int DEFAULT_TIMEOUT = 0;

    public static double F_POS_COLOR_WHEEL = 0;
    public static double P_POS_COLOR_WHEEL = 0;
    public static double I_POS_COLOR_WHEEL = 0;
    public static double D_POS_COLOR_WHEEL = 0;

    public static final int VOLTAGE_COMPENSATION_LEVEL = 12;

    public static final Time VOLTAGE_RAMP_RATE_COLOR_WHEEL = new Time(.05, Time.Unit.SECOND);


    private ColorWheel(){
        if(EnabledSubsystems.COLOR_WHEEL_ENABLED){
            colorWheelVictor = new VictorSPX(RobotMap.COLOR_WHEEL_VICTOR);

            colorWheelVictor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

            colorWheelVictor.setNeutralMode(NeutralMode.Brake);

            colorWheelVictor.enableVoltageCompensation(true);
            colorWheelVictor.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL);

            colorWheelVictor.configOpenloopRamp(VOLTAGE_RAMP_RATE_COLOR_WHEEL.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
            colorWheelVictor.configClosedloopRamp(VOLTAGE_RAMP_RATE_COLOR_WHEEL.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);

            colorWheelVictor.set(ControlMode.PercentOutput, 0);

        }
        SmartDashboardInit();
    }

    public synchronized static void init(){
        if(singleton == null){
            singleton = new ColorWheel();
        }
    }

    public static ColorWheel getInstance(){
        if(singleton == null){
            init();
        }
        return singleton;
    }

    public void disable(){
        if(colorWheelVictor != null){
            colorWheelVictor.set(ControlMode.PercentOutput, 0);
        }
    }

    public void SmartDashboardInit(){
        if(EnabledSubsystems.COLOR_WHEEL_SMARTDASHBOARD_ENABLED){
        
        SmartDashboard.putString("Sensor Target Color", GameData.targetColor.name());
        SmartDashboard.putString("Sensor Color", EnabledSensors.getInstance().colorSensor.getColor().name());

        SmartDashboard.putString("sensor red ", "" + EnabledSensors.getInstance().colorSensor.sensorColors()[0]);
        SmartDashboard.putString("sensor green ", "" + EnabledSensors.getInstance().colorSensor.sensorColors()[1]);
        SmartDashboard.putString("sensor blue ", "" + EnabledSensors.getInstance().colorSensor.sensorColors()[2]);
        //sensor information, game information??
        }
    }
    
    public void smartDashboardInfo() {
    SmartDashboard.putString("Sensor Color", EnabledSensors.getInstance().colorSensor.getColor().name());

    SmartDashboard.putString("sensor red ", "" + EnabledSensors.getInstance().colorSensor.sensorColors()[0]);
    SmartDashboard.putString("sensor green ", "" + EnabledSensors.getInstance().colorSensor.sensorColors()[1]);
    SmartDashboard.putString("sensor blue ", "" + EnabledSensors.getInstance().colorSensor.sensorColors()[2]);
      //update sensor information, sensor value

    }

    public void setMotorSpeedInPercent(double percent){
        System.out.println("colorwheel percent: " + percent);

        if(colorWheelVictor != null){
            colorWheelVictor.set(ControlMode.PercentOutput, percent);
        }
    }

}