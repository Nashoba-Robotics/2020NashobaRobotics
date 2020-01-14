package edu.nr.lib.commandbased;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

//Might not be necessary 
public class NRParallelCommandGroup extends ParallelCommandGroup
{

    private ArrayList<Command> commandGroup;

    public NRParallelCommandGroup()
    {
        super();
    }

    public NRParallelCommandGroup(Command... commandList)
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