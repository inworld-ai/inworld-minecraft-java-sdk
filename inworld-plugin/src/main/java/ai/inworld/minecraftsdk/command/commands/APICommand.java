package ai.inworld.minecraftsdk.command.commands;

import ai.inworld.minecraftsdk.command.Command;
import ai.inworld.minecraftsdk.command.CommandBase;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.MessageService;
import ai.inworld.minecraftsdk.services.ServerService;

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
        this.description = "Get or Set the Inworld REST API Keys";
        this.name = "api";
        this.version = "1.0";
        this.syntax = "/inworld api [item] [host]";
        this.minArgs = 2;
        this.maxArgs = 4;

        final List<String> commandList = new ArrayList<>();
        commandList.add("set");
        commandList.add("clear");
        commandList.add("list");
        tabCompletes.put(2, commandList);

        final List<String> itemList = new ArrayList<>();
        itemList.add("key");
        itemList.add("secret");
        tabCompletes.put(3, itemList);

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
            MessageService.sendPlayerMessage(sender, "API Host List:");
            for(String item : ConfigService.getConfig().getConfigurationSection("server.api").getKeys(false)) {
                String host = ConfigService.getConfig().getString("server.api." + item);
                MessageService.sendPlayerMessage(sender, "- " + item + ": " + host);
            }
            return;
        }

        // Handles the 'clear' command
        if ( args.length == minArgs + 1 && command.equals("clear") ) {
            String item = args[2];
            ConfigService.getConfig().set("server.api." + item, "");
            ConfigService.save();
            MessageService.sendPlayerMessage(sender, "API Host " + item + " has been cleared");
            // ServerService.restart();
            return;
        }

        // Handles the 'set' commmand
        if ( args.length == maxArgs && command.equals("set")) {
            String item = args[2];
            if (item.equals("key") || item.equals("secret") ) {
                String value = args[3];
                ConfigService.getConfig().set("server.api." + item, value);
                ConfigService.save();
                MessageService.sendPlayerMessage(sender, "API " + item + " has been set to " + value);
                // ServerService.restart();
                return;
            }
            MessageService.sendPlayerMessage(sender, "ERROR API item: " + item + " is not valid. Must be \'key\' or \'secret\'");
            return;
        }

    }

}
