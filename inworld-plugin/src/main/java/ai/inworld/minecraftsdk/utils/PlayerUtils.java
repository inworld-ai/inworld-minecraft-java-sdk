package ai.inworld.minecraftsdk.utils;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

public class PlayerUtils {

    public static String getUID(CommandSender sender) throws Exception {
        if (sender instanceof Player) {
            return ((Player) sender).getUniqueId().toString();
        }
        throw new Exception("Sender is not player."); 
    }

    public static void setFace(Player player, Villager villager) {
        Location playerLocation = player.getLocation();
        Location entityLocation = villager.getLocation();
        Vector dirBetweenLocations = playerLocation.toVector().subtract(entityLocation.toVector());//.multiply(-1)
        entityLocation.setDirection(dirBetweenLocations);
        // entityLocation.setX(entityLocation.getX());
        // entityLocation.setY(entityLocation.getY());
        // entityLocation.setZ(entityLocation.getZ());
        villager.teleport(entityLocation);
    }

}
