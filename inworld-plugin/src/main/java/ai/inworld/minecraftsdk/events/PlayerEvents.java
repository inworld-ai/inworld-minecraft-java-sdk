package ai.inworld.minecraftsdk.events;

import ai.inworld.minecraftsdk.services.CharacterService;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.SessionService;
import ai.inworld.minecraftsdk.session.Session;

import static ai.inworld.minecraftsdk.utils.PlayerUtils.setFace;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * The event handlers for Player events.
 */
public class PlayerEvents implements Listener {

    /**
     * This handler is used to detect when a player right clicks on an entity
     * @param event The event thrown
     */
    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {
        
        // Iterate through the Inworld characters to find the one the player clicked on
        for(String characterId : ConfigService.getConfig().getConfigurationSection("server.characters").getKeys(false)) {
            // TODO Change this to use the Character Service
            String uid = ConfigService.getConfig().getString("server.characters." + characterId + ".uid");
            // Compare the Villager entity UID with the Character UID
            if (uid.equals(event.getRightClicked().getUniqueId().toString())) {
                // Prevent the right click from opening a trade window
                event.setCancelled(true);
                Player player = event.getPlayer();
                // Wake the Villager up if it was sleeping
                Villager villager = (Villager) event.getRightClicked();
                if (villager.isSleeping()) {
                    villager.wakeup();
                }
                // Shuts the Villager AI and Awareness off so it stops moving
                villager.setAI(false);
                villager.setAware(false);
                // Direct the Villager to turn and look at the Player
                setFace(event.getPlayer(), villager);
                LOG(LogType.Info, event.getPlayer().getName() + " right clicked " + villager.getCustomName());
                // Get the Villager's Inworld CharacterID and open a session
                String id = CharacterService.getIdByUid(villager.getUniqueId().toString());
                try {
                    SessionService.addSession(id, player);
                } catch (RuntimeException e) {
                    player.sendMessage(ChatColor.RED + "Error" + ": " + ChatColor.GRAY + "» " + ChatColor.WHITE + e.getMessage());
                }
                break;
            }
        }

    }

    /**
     * This handler is used when a player joins to start the thread that 
     * retrieves events from the Inworld REST service.
     * @param event The event thrown
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LOG(LogType.Info, "Player joined " + player.getPlayerListName());
        // ServerService.start();
    }

    /**
     * This handler checks if the player moves away from the Villager and
     * clears the active session from the system but doesn't close it.
     * @param event The event thrown
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        
        // Checks if the player moving has an open Inworld session
        if (SessionService.hasPlayerSession(player)) {

            Location fromLoc = event.getFrom();
            Location toLoc = event.getTo();
    
            // LOG(LogType.Info, "Player moved from: " + event.getFrom());
            // LOG(LogType.Info, "Player moved to: " + event.getTo());
            
            Double diffX = fromLoc.getX() - toLoc.getX();
            Double diffY = fromLoc.getY() - toLoc.getY();
            Double diffZ = fromLoc.getZ() - toLoc.getZ();
            
            // Determines if a player has moved. Currently is allows any XY movement
            if ( diffX > 0 || diffY > 0 || diffX > 0) {
                // LOG(LogType.Info, "Player moved");
                // Clears the session but does not close it
                SessionService.clearPlayerSession(player);
            }

        }

    }

    /**
     * This handler runs when a player quits. It clears and closes all open session
     * for the player and if there are no other players on the server stops the thread
     * that constantly pulls the list of events from the Inworld REST service.
     * @param event The event thrown
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Clears and closes all session for the player
        SessionService.removePlayer(player);
        LOG(LogType.Info, "Player quit " + player.getPlayerListName());
        List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
        // LOG(LogType.Info, "Player list " + list.size());
        // Stopping the thread if the last person leaves the server. 
        // Note: The size of users will be 1 as the last person leaves because this is called before
        // the event reaches the Minecraft server.
        if (list.size() <= 1) {
            // ServerService.stop();
        }
    }

    /**
     * This handler runs when a player chats using the default chat system. It checks 
     * if the player is in an active session and if so it sends to the message to the
     * Inworld REST service.
     * @param event The event thrown
     */
    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        
        Player player = event.getPlayer();
        
        // Check if the player is actively chatting with a character
        if (SessionService.hasPlayerSession(player)) {
            // Stops the chat event from processing so it doesn't display to every other 
            // Player on the server
            event.setCancelled(true);
            String message = event.getMessage();
            // LOG(LogType.Info, "Player message " + player.getPlayerListName() + " " + message);
            try {
                // Gets the current active session
                Session session = SessionService.getSession(SessionService.getPlayerSessionId(player));
                // Sends the player message to the player
                player.sendMessage(ChatColor.GREEN + player.getDisplayName() + ": " + ChatColor.GRAY + "» " + ChatColor.WHITE + message);
                // Sends the player message to the Inworld REST service.
                SessionService.sendMessage(session, player, message);
            } catch( RuntimeException e ) {
                player.sendMessage(ChatColor.RED + e.getMessage());
            }

        }

    }

}
