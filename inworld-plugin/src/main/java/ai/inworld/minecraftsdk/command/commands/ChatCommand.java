package ai.inworld.minecraftsdk.command.commands;

import ai.inworld.minecraftsdk.command.Command;
import ai.inworld.minecraftsdk.command.CommandBase;

import ai.inworld.minecraftsdk.services.APIService;
import ai.inworld.minecraftsdk.services.SessionService;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import ai.inworld.minecraftsdk.utils.StringUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

/**
 * Handles the /chat command used talking to the character. 
 * Currently not needed as direct chat is possible
 * @deprecated
 */
public class ChatCommand extends CommandBase implements Command {
    
    public ChatCommand() {

        this.permission = "chat.command";
        this.description = "Sends a message to a character";
        this.name = "chat";
        this.version = "1.0";
        this.syntax = "/chat [message]";
        this.minArgs = 1;
        this.maxArgs = 255;
    
        final List<String> commandList = new ArrayList<>();
        commandList.add("message");
        tabCompletes.put(1, commandList);

    }

    /**
     * @param sender The Player that is sending the command
     * @param args The array of arguments sent
     */
    @Override
    protected void processCommand(Player sender, String[] args) {

        LOG(LogType.Info, "Chat " + StringUtils.arrayToString(args));
        String message = StringUtils.arrayToString(args);
        try {
            APIService.message(SessionService.getPlayerSessionId(sender), message);
        } catch ( ConnectException e ) {
            LOG(LogType.Error, "ServerService ConnectException: " + e.getMessage());
        } catch ( IOException e ) {
            LOG(LogType.Error, "ServerService IOException: " + e.getMessage());
        } catch( RuntimeException e ) {
            LOG(LogType.Error, "ServerService RuntimeException: " + e.getMessage());
        }

    }

    /**
     * @param sender The Player that is sending the command
     * @param args The array of arguments sent
     */
    @Override
    public void perform(Player player, String[] args) {
        processCommand(player, args);
    }

    /**
     * @param sender The Player that is sending the command
     * @param args The array of arguments sent
     */
    @Override
    public void performConsole(Player sender, String[] args) {
        processCommand(sender, args);
    }

}
