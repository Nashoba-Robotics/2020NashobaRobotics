package edu.nr.lib.commandbased;
 
import java.util.ArrayList;
 
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
 
public class NRCommandGroup extends CommandGroupBase {
 
    private ArrayList<NRCommand> commands;
    private boolean canceled = false;
 
    public NRCommandGroup(ArrayList<NRCommand> commandz){
        super();
        this.commands = commandz;
    }
 
    public void runNRCommandGroup(){
        for(int i = 0; i < commands.size(); i++){
            if (canceled != true)
            {
            commands.get(i).execute();
            }
        }
        canceled = false;
    }
 
    public void cancelCommandGroup()
    {
        /*
        for(int i = 0; i < commands.size(); i++){
            
            commands.get(i).cancel();
 
        }
        */
        //Test out if commands to be run are also canceled
        canceled = true;
    }
 
    public void addCommands(Command... commands) 
    {
        for(int i = 0; i < commands.length; i++)
        {
            this.commands.add((NRCommand)commands[i]);
        }
    }
}

