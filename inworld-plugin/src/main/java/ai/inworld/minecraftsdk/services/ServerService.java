package ai.inworld.minecraftsdk.services;

import ai.inworld.minecraftsdk.session.Session;
import ai.inworld.minecraftsdk.utils.NetUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

/**
 * This service class handles the threaded process that retrieves events from the Inworld
 * If events are found then it messages the associated player.
 * REST service
 */
public final class ServerService {
    
    public static final String SERVER_ID = UUID.randomUUID().toString();
    public static final String SERVER_IP = NetUtils.getIp();
    public static BukkitRunnable eventThread;
    private static JavaPlugin plugin;

    /**
     * This method is called upon enabling of the plugin. It checks if there are an players
     * already on the server and if so then starts the threaded process
     * @param plugin A reference to the Minecraft plugin
     */
    public ServerService(JavaPlugin plugin) {

        ServerService.plugin = plugin;

        // If there are players on the server start the process
        if (ServerService.plugin.getServer().getOnlinePlayers().size() > 0) {
            start();
        }

    }

    /**
     * This method starts the threaded process
     * @returns Nothing.
     */
    public static void start() {
        LOG(LogType.Info, "ServerService start events process");
        
        try {
            APIService.getAPIHost();
        } catch (RuntimeException e) {
            for (Player player: ServerService.plugin.getServer().getOnlinePlayers()) {
                player.sendMessage(ChatColor.RED + "Error: " + ChatColor.GRAY + "» " + ChatColor.WHITE + "Inworld REST Host Not Configured");
            }
            return;
        }

        // if (.equals("")) {
            
        //     return;
        // }

        // If the thread is null then run
        if (eventThread == null) {

            eventThread = new BukkitRunnable() {
                @Override
                public void run() {
    
                    try {
                        
                        // Retrieve any events and if found then process and send them to their associated player
                        ArrayList<JSONObject> events = APIService.getEvents();
                        for( JSONObject event: events) {
                            if (event.get("type").toString().equals("text")) {
                                String sessionId = event.get("sessionId").toString();
                                String message = event.get("text").toString();
                                Session session = SessionService.getSession(sessionId);
                                session.getPlayer().sendMessage(ChatColor.RED + session.getDisplayName() + ": " + ChatColor.GRAY + "» " + ChatColor.WHITE + message);
                            }
                        }
    
                        // Uncomment below to detail out the events recieved
                        // LOG(LogType.Info, "ServerService events: " + events.toString());
    
                    } catch ( ConnectException e ) {
                        LOG(LogType.Error, "ServerService ConnectException: " + e.getMessage());
                    } catch ( IOException e ) {
                        LOG(LogType.Error, "ServerService IOException: " + e.getMessage());
                    } catch( RuntimeException e ) {
                        LOG(LogType.Error, "ServerService RuntimeException: " + e.getMessage());
                    }
    
                }
            };
            eventThread.runTaskTimerAsynchronously(plugin, 20, 5);
    
        }

    }

    /**
     * This method stops the threaded process
     */
    public static void stop() {
        // LOG(LogType.Info, "ServerService stop events process");
        // If the thread is not null then stop it
        if (eventThread != null) {
            eventThread.cancel();
            eventThread = null;
        } 
    }

    /**
     * This method restarts the threaded process
     */
    public static void restart() { 
        stop();
        start();
    }

}
