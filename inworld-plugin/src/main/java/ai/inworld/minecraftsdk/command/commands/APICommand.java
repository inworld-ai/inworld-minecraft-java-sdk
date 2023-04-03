package ai.inworld.minecraftsdk.command.commands;

import ai.inworld.minecraftsdk.command.Command;
import ai.inworld.minecraftsdk.command.CommandBase;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.MessageService;

import org.bukkit.entity.Player;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the /inworld api command used in setting the dev and prod API hosts.
 * Command List:
 * - 'list' Lists the dev and prod hosts
 * - 'set' Sets the host for the specific dev or prod
 * - 'clear' Clears the host for the specific dev or prod
 */
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

    /**
     * @param sender The Player that is sending the command
     * @param args The array of arguments sent
     */
    @Override
    protected void processCommand(Player sender, String[] args) {
        
        LOG(LogType.Info, "The api command has been run");

        String command = args[1];

        // Handles the 'list' command
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

        // Handles the 'clear' command
        if ( args.length == minArgs + 1 && command.equals("clear") ) {
            String env = args[2];
            ConfigService.getConfig().set("server.api." + env, "");
            ConfigService.save();
            MessageService.sendPlayerMessage(sender, "API Host " + env + " has been cleared");
            return;
        }

        // Handles the 'set' commmand
        if ( args.length == maxArgs && command.equals("set")) {
            String env = args[2];
            if (env.equals("dev") || env.equals("prod") ) {
                String host = args[3];
                ConfigService.getConfig().set("server.api." + env, host);
                ConfigService.save();
                MessageService.sendPlayerMessage(sender, "API Host " + env + " has been set to " + host);
                return;
            }
            MessageService.sendPlayerMessage(sender, "ERROR API Enironment " + env + " is not valid. Must be \'dev\' or \'prod\'");
            return;
        }

    }

}
