package edu.nr.lib.commandbased;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;

public class NRCommandGroup extends CommandGroupBase {

    private ArrayList<NRCommand> commands;

    public NRCommandGroup(ArrayList<NRCommand> commandz){
        super();
        this.commands = commandz;
    }

    public void runNRCommandGroup(){
        for(int i = 0; i < commands.size(); i++){
            
            commands.get(i).execute();

        }
    }

    

}