package ai.inworld.minecraftsdk.services;
import ai.inworld.minecraftsdk.utils.NetUtils;
import java.util.UUID;
import org.bukkit.plugin.java.JavaPlugin;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

/**
 * This service class handles the server information
 * REST service
 */
public final class ServerService {
    
    public static final String SERVER_ID = UUID.randomUUID().toString();
    public static final String SERVER_IP = NetUtils.getIp();
    private static JavaPlugin plugin;

    /**
     * This method is called upon enabling of the plugin.
     * @param plugin A reference to the Minecraft plugin
     */
    public ServerService(JavaPlugin plugin) {
        ServerService.plugin = plugin;
    }

}
