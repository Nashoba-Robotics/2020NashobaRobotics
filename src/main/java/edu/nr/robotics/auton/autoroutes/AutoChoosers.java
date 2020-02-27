package edu.nr.robotics.auton.autoroutes;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutoChoosers
{
    public static SendableChooser<startPos> startPosChooser = new SendableChooser<startPos>();
    public static SendableChooser<ballLocation> ballLocationChooser = new SendableChooser<ballLocation>();

    public enum startPos
    {
        directlyInFrontOfGoal, directlyInFrontOfTrench, twoPos, threePosition, middleOfNowhere;
    }

    public enum ballLocation
    {
        none, trench, twoRendezvous, threeRendezvous;
    }
}