package edu.nr.lib.commandbased;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.ArrayList;

public class NRSequentialCommandGroup extends SequentialCommandGroup
{

    private ArrayList<Command> commandGroup;

    public NRSequentialCommandGroup()
    {
        super();
    }

    public NRSequentialCommandGroup(Command... commandList)
    {
        super(commandList);
    }

    @Override
    public void initialize()
    {

    }

    @Override
    public void execute()
    {

    }

    @Override
    public void end(boolean interrupted)
    {

    }

    @Override
    public boolean isFinished()
    {
        return true;
    }



}