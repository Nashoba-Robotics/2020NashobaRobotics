package edu.nr.robotics;

public class RobotMap {

    public static final int ARM_TALON = 15;

    public static final int RIGHT_DRIVE = 5;  //define these, should be going to 12
    public static final int LEFT_DRIVE = 6;  // should be 0 //6
    public static final int RIGHT_DRIVE_FOLLOW_1 = 2;  //define these
    public static final int RIGHT_DRIVE_FOLLOW_2 = 4;  
    public static final int LEFT_DRIVE_FOLLOW_1 = 3; // was 1
    public static final int LEFT_DRIVE_FOLLOW_2 = 1;
    //public static final int RIGHT_DRIVE_FOLLOW_1_CURRENT = 2;
    //public static final int RIGHT_DRIVE_FOLLOW_2_CURRENT = 0;
    //public static final int LEFT_DRIVE_FOLLOW_1_CURRENT = 15;
    //public static final int LEFT_DRIVE_FOLLOW_2_CURRENT = 14;
    public static final int TURRET_TALON = 0;
    public static final int TRANSFER_TALON = 1;
    public static final int HOOD_TALON = 5;

    public static final int SHOOTER_TALON1 = 9; // falcon 500 motor on 2019 bot at pdp six is slot 0
    public static final int SHOOTER_TALON2 = 1;
    public static final int PIGEON_ID = 10;

    public static final int PCM_ID = 1;
    public static final int PDP_ID = 62;

    public static final int LIM_HOOD_UPPER = 6;
    public static final int LIM_HOOD_LOWER = 1;

    public static final int LIM_TURRET_LEFT = 2;
    public static final int LIM_TURRET_RIGHT = 3;

    public static final int INDEXER_PUKE_SENSOR = 4;
    public static final int INDEXER_SETTING1 = 1;
    public static final int INDEXER_SETTING2 = 2;
    public static final int INDEXER_SETTING3 = 3;
    public static final int INDEXER_SHOOTER_SENSOR = 0; // real for robot
    public static final int INDEXER_TALON = 20; //7
    
    public static final int TRANSFER_SENSOR = 5;
    public static final int TRANSFER_SPARK = 1000;
    public static final int TRANSFER_VICTOR = 14;

    //Change to thing
    public static final int INTAKE_TALON = 0;
    public static final int INTAKE_SOLENOID_PCM_PORT = 0;
    
    public static final int CLIMB_DEPLOY_VICTOR = 4;
    public static final int CLIMB_DEPLOY_CHANNEL = 100;
    
    public static final int WINCH_TALON = 3;

    public static final int BASH_BAR_SOLENOID = 7;//pcm for 2019 bot

    public static final int COLOR_WHEEL_VICTOR = 0;
    public static final int COLOR_WHEEL_SOLENOID = 0;

//    public static final int INTAKE_SPARKMAX = 0;
//    public static final int INTAKE_SOLENOID_PCM_PORT = 0;

    ///sensor ports too
}