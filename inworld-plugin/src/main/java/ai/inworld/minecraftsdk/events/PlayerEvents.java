package ai.inworld.minecraftsdk.events;

import ai.inworld.minecraftsdk.services.APIService;
import ai.inworld.minecraftsdk.services.CharacterService;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.SessionService;
import ai.inworld.minecraftsdk.services.ServerService;

import static ai.inworld.minecraftsdk.utils.PlayerUtils.setFace;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {
        
        for(String characterId : ConfigService.getConfig().getConfigurationSection("server.characters").getKeys(false)) {
            // TODO Change this to use the Character Service
            String uid = ConfigService.getConfig().getString("server.characters." + characterId + ".uid");
            if (uid.equals(event.getRightClicked().getUniqueId().toString())) {
                event.setCancelled(true);
                Villager villager = (Villager) event.getRightClicked();
                if (villager.isSleeping()) {
                    villager.wakeup();
                }
                villager.setAI(false);
                villager.setAware(false);
                setFace(event.getPlayer(), villager);
                LOG(LogType.Info, event.getPlayer().getName() + " right clicked " + villager.getCustomName());
                String id = CharacterService.getIdByUid(villager.getUniqueId().toString());
                SessionService.addSession(id, event.getPlayer());
                break;
            }
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LOG(LogType.Info, "Player joined " + player.getPlayerListName());
        ServerService.start();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        
        if (SessionService.hasPlayerSession(player)) {

            Location fromLoc = event.getFrom();
            Location toLoc = event.getTo();
    
            // LOG(LogType.Info, "Player moved from: " + event.getFrom());
            // LOG(LogType.Info, "Player moved to: " + event.getTo());
    
            Double diffX = fromLoc.getX() - toLoc.getX();
            Double diffY = fromLoc.getY() - toLoc.getY();
            Double diffZ = fromLoc.getZ() - toLoc.getZ();
    
            if ( diffX > 0 || diffY > 0 || diffX > 0) {
                // LOG(LogType.Info, "Player moved");
                SessionService.clearPlayerSession(player);
            }

        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SessionService.removePlayer(player);

        // TODO Close all players sessions
        
        LOG(LogType.Info, "Player quit " + player.getPlayerListName());
        List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
        // LOG(LogType.Info, "Player list " + list.size());
        // Stopping the thread if the last person leaves the server. 
        // Note: The size of users will be 1 as the last person leaves because this is called before
        // the event reaches the Minecraft server.
        if (list.size() <= 1) {
            ServerService.stop();
        }
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        
        Player player = event.getPlayer();
        
        // Check if the player is actively chatting with a character
        if (SessionService.hasPlayerSession(player)) {
            event.setCancelled(true);
            String message = event.getMessage();
            LOG(LogType.Info, "Player message " + player.getPlayerListName() + " " + message);
            try {
                player.sendMessage(ChatColor.GREEN + player.getDisplayName() + ": " + ChatColor.GRAY + "» " + ChatColor.WHITE + message);
                SessionService.sendMessage(player, message);
            } catch( RuntimeException e ) {
                player.sendMessage(ChatColor.RED + e.getMessage());
            }

        }

    }

}
