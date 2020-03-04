package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class InvertDriveCommand extends CommandBase
{
    public InvertDriveCommand()
    {
        
    }

    @Override
    public void execute()
    {
        Drive.getInstance().invertDrive();
    }

    @Override
    public boolean isFinished()
    {
        return true;
    }
}