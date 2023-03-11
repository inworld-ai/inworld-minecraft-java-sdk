package ai.inworld.minecraftsdk.command.commands;

import ai.inworld.minecraftsdk.command.Command;
import ai.inworld.minecraftsdk.command.CommandBase;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.MessageService;

import static ai.inworld.minecraftsdk.utils.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.Logger.LogType;

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
        this.maxArgs = 4;

        final List<String> commandList = new ArrayList<>();
        commandList.add("set");
        commandList.add("clear");
        commandList.add("list");
        tabCompletes.put(2, commandList);

        final List<String> environmentList = new ArrayList<>();
        environmentList.add("dev");
        environmentList.add("prod");
        tabCompletes.put(3, environmentList);

    }

    @Override
    protected void processCommand(CommandSender sender, String[] args) {
        
        LOG(LogType.Info, "The api command has been run");

        String command = args[1];

        if ( args.length == minArgs && command.equals("list") ) {
            // String env = args[2];
            // String host = ConfigService.getConfig().getString("server.api." + env);
            // MessageService.sendPlayerMessage(sender, host);
            MessageService.sendPlayerMessage(sender, "API Host List:");
            for(String env : ConfigService.getConfig().getConfigurationSection("server.api").getKeys(false)) {
                String host = ConfigService.getConfig().getString("server.api." + env);
                MessageService.sendPlayerMessage(sender, "- " + env + ": " + host);
            }
            return;
        }

        if ( args.length == minArgs + 1 && command.equals("clear") ) {
            String env = args[2];
            ConfigService.getConfig().set("server.api." + env, "");
            ConfigService.save();
            MessageService.sendPlayerMessage(sender, "API Host " + env + " has been cleared");
            return;
        }

        if ( args.length == maxArgs && command.equals("set")) {
            String env = args[2];
            if (env.equals("dev") || env.equals("prod") ) {
                String host = args[3];
                ConfigService.getConfig().set("server.api." + env, host);
                ConfigService.save();
                MessageService.sendPlayerMessage(sender, "API Host " + env + " has been set to " + host);
                return;
            }
            MessageService.sendPlayerMessage(sender, "ERROR API Enironment " + env + " is not valid. Must be dev or prod");
            return;
        }

    }

}
