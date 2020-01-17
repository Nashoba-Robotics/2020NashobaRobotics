package edu.nr.robotics.subsystems.hood;

public class limelightMath
{
    private int limelightHeight;
    private int targetHeight;
    private int mountingAngle;

    private int givenLimelightAngle;

    public limelightMath()
    {

    }

    public void math()
    {
        //Angles are vertical and in degrees
        //distance is distance from limelight to target
        //givenLimelightAngle is angle returned from limelight
        int distance = (targetHeight - limelightHeight) / /*tan*/ (mountingAngle + givenLimelightAngle);
    }
}