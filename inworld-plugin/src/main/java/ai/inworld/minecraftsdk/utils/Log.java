package ai.inworld.minecraftsdk.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static ai.inworld.minecraftsdk.Constants.pluginName;

public class Log {

    public enum LogType {
        Info,
        Error
    }

    public static void logConsole(LogType logType, String msgInput) {
        String msgOutput = formatMessage(logType, msgInput);

        switch(logType) {
            case Error:
                Bukkit.getLogger().warning(msgOutput);
                break;
            case Info:
            default:
                Bukkit.getLogger().info(msgOutput);
                break;
        }
    }

    public static void logSendMessage(LogType logType, CommandSender sender, String msgInput) {
        String msgOutput = formatMessage(logType, msgInput);

        if (sender instanceof Player) {
            sender.sendMessage(msgOutput);
        }

        logConsole(logType, msgInput);
    }

    private static String formatMessage(LogType logType, String msgInput) {
        String msgOutput = "[" + pluginName + "] " + msgInput;

        switch(logType) {
            case Error:
                return ChatColor.RED + msgOutput;
            case Info:
            default:
                return ChatColor.GREEN + msgOutput;
        }
    }

}
