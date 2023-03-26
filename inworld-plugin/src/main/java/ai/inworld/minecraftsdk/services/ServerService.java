package ai.inworld.minecraftsdk.services;

import ai.inworld.minecraftsdk.services.APIService;
import ai.inworld.minecraftsdk.services.SessionService;
import ai.inworld.minecraftsdk.session.Session;
import ai.inworld.minecraftsdk.utils.NetUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

public final class ServerService {
    
    public static final String SERVER_ID = UUID.randomUUID().toString();
    public static final String SERVER_IP = NetUtils.getIp();
    public static BukkitRunnable eventThread;
    private static JavaPlugin plugin;

    public ServerService(JavaPlugin plugin) {

        ServerService.plugin = plugin;

        if (ServerService.plugin.getServer().getOnlinePlayers().size() > 0) {
            start();
        }

    }

    public static void start() {
        LOG(LogType.Info, "ServerService start events process");
        
        if (eventThread == null) {

            eventThread = new BukkitRunnable() {
                @Override
                public void run() {
    
                    try {
    
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
                        LOG(LogType.Info, "ServerService events: " + events.toString());
    
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

    public static void stop() {
        // LOG(LogType.Info, "ServerService stop events process");
        if (eventThread != null) {
            eventThread.cancel();
            eventThread = null;
        } 
        // try {
            
        // } catch ( IllegalStateException e ) {
        //     // Ignore error if the plugin is started or stopped without it having been run before.
        //     if ( e.getMessage() != "Not scheduled yet") {
        //         throw e;
        //     }
            
        // }
    }

}
