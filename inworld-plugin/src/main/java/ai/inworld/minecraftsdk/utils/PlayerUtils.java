package ai.inworld.minecraftsdk.utils;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

/**
 * This class is a helper for Player commands
 */
public class PlayerUtils {

    /**
     * This method gets the UUID of a Player sending a command
     * Not currently used
     * @param sender The Player sending the command
     * @return String The string representation of the Player UUID
     * @throws Exception Thrown if the sender is not a Player
     */
    public static String getUID(CommandSender sender) throws Exception {
        if (sender instanceof Player) {
            return ((Player) sender).getUniqueId().toString();
        }
        throw new Exception("Sender is not player."); 
    }

    /**
     * This method turns a Villager to look at a Player
     * @param player The Player to turn the Villager to
     * @param villager The Villager to turn
     */
    public static void setFace(Player player, Villager villager) {
        Location playerLocation = player.getLocation();
        Location entityLocation = villager.getLocation();
        Vector dirBetweenLocations = playerLocation.toVector().subtract(entityLocation.toVector());//.multiply(-1)
        entityLocation.setDirection(dirBetweenLocations);
        villager.teleport(entityLocation);
    }

}
