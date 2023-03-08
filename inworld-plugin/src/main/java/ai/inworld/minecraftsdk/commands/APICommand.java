package ai.inworld.minecraftsdk.commands;

import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.MessageService;

import static ai.inworld.minecraftsdk.utils.Log.logConsole;
import static ai.inworld.minecraftsdk.utils.Log.LogType;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class APICommand extends CommandBase implements Command {
    
    public APICommand() {
        
        this.permission = "inworld.command.api";
        this.description = "Get or Set the Inworld REST API URLs";
        this.name = "api";
        this.version = "1.0";
        this.syntax = "/inworld api [environment] [host]";
        this.minArgs = 2;
        this.maxArgs = 3;

        final List<String> environmentList = new ArrayList<>();
        environmentList.add("dev");
        environmentList.add("prod");
        tabCompletes.put(2, environmentList);

    }

    @Override
    protected void processCommand(CommandSender sender, String[] args) {
        
        logConsole(LogType.Info, "The api command has been run");

        if ( args.length == minArgs ) {
            String env = args[1];
            String host = ConfigService.getConfig().getString("server.api." + env);
            MessageService.sendPlayerMessage(sender, host);
            return;
        }

        if ( args.length == maxArgs ) {
            String env = args[1];
            String host = args[2];
            ConfigService.getConfig().set("server.api." + env, host);
            ConfigService.save();
            MessageService.sendPlayerMessage(sender, "Host " + env + " set to " + host);
            return;
        }

    }

}
