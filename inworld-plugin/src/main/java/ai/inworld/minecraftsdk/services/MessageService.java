package ai.inworld.minecraftsdk.services;

import org.bukkit.command.CommandSender;

import static ai.inworld.minecraftsdk.Constants.PLUGIN_NAME;

public class MessageService {

    public static void sendPlayerMessage(CommandSender commandSender, String message) {
        // TODO Use Bukkit ChatColor instead of §
        commandSender.sendMessage("§c§l" + PLUGIN_NAME + " §7§l» §7" + message);
    }

}