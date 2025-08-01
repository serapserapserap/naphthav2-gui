package seraph.base.commands.impl;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import scala.actors.threadpool.Arrays;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Command extends CommandBase {
    private String name;
    private List<String> aliases;
    private String usage;
    private int calls;

    public Command(String name, String description, String... aliases){
        this.name = name;
        this.usage = description;
        this.aliases = (List<String>)Arrays.asList(aliases);
    }

    public Command(String name, String description){
        this(name,description,new String[]{});
    }
    public Command (String name, String... aliases){
        this(name,"/"+name,aliases);
    }
    public Command(String name){
        this(name,"/"+name,new String[]{});
    }

    @Override
    public String getCommandName() {
        return "/"+name;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return this.usage;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        this.calls++;
        this.executeCommand(args);
    }

    @Override
    public List<String> getCommandAliases(){
        return this.aliases;
    }

    @Override
    public int getRequiredPermissionLevel(){
        return 0;
    }

    public abstract void executeCommand(String[] args);
}
