package ai.inworld.minecraftsdk.services;

import org.bukkit.command.CommandSender;

import static ai.inworld.minecraftsdk.Constants.PLUGIN_NAME;

/**
 * This service class handles sending messages to a Player
 */
public class MessageService {

    /**
     * This method sends a message to a Player
     * @param commandSender The Player to send the message to
     * @param message The message to send
     */
    public static void sendPlayerMessage(CommandSender commandSender, String message) {
        // TODO Use Bukkit ChatColor instead of §
        commandSender.sendMessage("§c§l" + PLUGIN_NAME + " §7§l» §7" + message);
    }

}