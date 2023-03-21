package ai.inworld.minecraftsdk.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static String getUID(CommandSender sender) throws Exception {
        if (sender instanceof Player) {
            return ((Player) sender).getUniqueId().toString();
        }
        throw new Exception("Sender is not player."); 
    }

}
