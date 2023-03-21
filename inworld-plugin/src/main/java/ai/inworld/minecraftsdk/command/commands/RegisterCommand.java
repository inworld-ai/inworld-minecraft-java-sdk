package ai.inworld.minecraftsdk.command.commands;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ai.inworld.minecraftsdk.command.Command;
import ai.inworld.minecraftsdk.command.CommandBase;

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
    protected void processCommand(Player sender, String[] args) {
        LOG(LogType.Info, "The registration command has been run");
    }

}
