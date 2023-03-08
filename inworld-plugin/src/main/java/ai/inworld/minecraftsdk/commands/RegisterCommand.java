package ai.inworld.minecraftsdk.commands;

import ai.inworld.minecraftsdk.commands.Command;
import ai.inworld.minecraftsdk.commands.CommandBase;

import static ai.inworld.minecraftsdk.utils.Log.logConsole;
import static ai.inworld.minecraftsdk.utils.Log.LogType;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand extends CommandBase implements Command {
    
    public RegisterCommand() {
        
        this.permission = "inworld.command.register";
        this.description = "Register the Admin user with the system";
        this.name = "register";
        this.version = "1.0";
        this.syntax = "/inworld register";
        this.minArgs = 1;
        this.maxArgs = 1;

    }

    @Override
    protected void processCommand(CommandSender sender, String[] args) {
        logConsole(LogType.Info, "The registration command has been run");
    }

}
