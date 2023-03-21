package ai.inworld.minecraftsdk.events;

import ai.inworld.minecraftsdk.services.ConfigService;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        
        for(String characterId : ConfigService.getConfig().getConfigurationSection("server.characters").getKeys(false)) {
            String uid = ConfigService.getConfig().getString("server.characters." + characterId + ".uid");
            if (uid.equals(event.getRightClicked().getUniqueId().toString())) {
                LOG(LogType.Info, event.getPlayer().getName() + " right clicked " + event.getRightClicked().getCustomName());
                break;
            }
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LOG(LogType.Info, "Player joined " + player.getPlayerListName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LOG(LogType.Info, "Player quit " + player.getPlayerListName());
        // plugin.getInworldCharacters().closeAllConnection(player);
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        LOG(LogType.Info, "Player message " + player.getPlayerListName() + " " + message);
        // plugin.getInworldCharacters().sendMessage(player, message);
    }

}
