package ai.inworld.minecraftsdk.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static ai.inworld.minecraftsdk.utils.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.Logger.LogType.Error;
import static ai.inworld.minecraftsdk.utils.Logger.LogType.Info;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LOG(Info, "Player joined " + player.getPlayerListName());
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LOG(Info, "Player quit " + player.getPlayerListName());
        // plugin.getInworldCharacters().closeAllConnection(player);
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        LOG(Info, "Player message " + player.getPlayerListName() + " " + message);
        // plugin.getInworldCharacters().sendMessage(player, message);
    }

}
