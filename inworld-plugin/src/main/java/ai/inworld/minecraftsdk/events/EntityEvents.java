package ai.inworld.minecraftsdk.events;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

public class EntityEvents implements Listener {
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        // event.setCancelled(true);
        // // event.setDamage(0);
        // Entity entity = event.getEntity();
        // LOG(LogType.Info, "Entity damaged: " + entity.getEntityId());
    }

}
