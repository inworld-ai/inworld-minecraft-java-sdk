package ai.inworld.minecraftsdk.utils.logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static ai.inworld.minecraftsdk.Constants.PLUGIN_NAME;

/**
 * This class is a helper for logging output to the server
 */
public class Logger {

    // The types of logging output
    public enum LogType {
        Info,
        Error
    }

    /**
     * This method is for logging output to the server
     * @param logType The type of log
     * @param msgInput The message to output
     */
    public static void LOG(LogType logType, String msgInput) {
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

    /**
     * This method sends a log output to the player
     * Not currently used
     * @deprecated
     * @param logType The type of message to send
     * @param sender The player to send the message to
     * @param msgInput The message to send
     */
    public static void logSendMessage(LogType logType, CommandSender sender, String msgInput) {
        String msgOutput = formatMessage(logType, msgInput);

        if (sender instanceof Player) {
            sender.sendMessage(msgOutput);
        }

        LOG(logType, msgInput);
    }

    /**
     * This method formats the message for output
     * @param logType The type of log
     * @param msgInput The message to output
     * @return String The formatted message to send
     */
    private static String formatMessage(LogType logType, String msgInput) {

        String msgOutput = "[" + PLUGIN_NAME + "] " + msgInput;

        switch(logType) {
            case Error:
                return msgOutput;
            case Info:
            default:
                return msgOutput;
        }
        
    }

}
