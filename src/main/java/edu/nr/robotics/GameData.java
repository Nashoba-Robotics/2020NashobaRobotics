package edu.nr.robotics;

import edu.nr.robotics.subsystems.sensors.ISquaredCSensor.SensorColor;
import edu.wpi.first.wpilibj.DriverStation;
    //Ethan has THE SMALLEST arms

public class GameData{

    public static SensorColor targetColor = SensorColor.LemonChiffon;

    private static GameData walter;// walter CAD-ed our entire robot in 2020. A legend. Use this class to remember his name.

    public static GameData getInstance(){
        if(walter == null){
            init();
        }
        return walter;
    }

    private GameData(){
//hehehehe this does nothing
    }

    public static synchronized void init(){
        if(walter == null){
            walter = new GameData();
            walter.getGameData();
        }
    }

    public void getGameData(){
        char data = 'r'; // make u for real code, changed for testing
      if(DriverStation.getInstance().getGameSpecificMessage().length() > 0){
        data = DriverStation.getInstance().getGameSpecificMessage().charAt(0);
      }

        System.out.println("GAME DATA: " + data);

        targetColor = SensorColor.LemonChiffon;
        
        if(data == 'r' || data == 'R'){
            targetColor = SensorColor.Red;
        }

        else if(data == 'g' || data == 'G'){
            targetColor = SensorColor.Green;
        }

        else if(data == 'b' || data == 'B'){
            targetColor = SensorColor.Blue;
        }
        
        else if(data == 'y' || data == 'Y'){
            targetColor = SensorColor.Yellow;
        }
    }

}