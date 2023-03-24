package ai.inworld.minecraftsdk.services;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import ai.inworld.minecraftsdk.session.Session;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

public class SessionService {
    
    private static final HashMap<String, String> playerSessions = new HashMap<>();
    private static final HashMap<String, Session> sessions = new HashMap<>();
    private static final HashMap<String, String> sessionKeys = new HashMap<>();

    private static JavaPlugin plugin;

    public SessionService(JavaPlugin plugin) {

        SessionService.plugin = plugin;
    }

    public static Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public static String getPlayerSessionId(Player player) {
        return playerSessions.get(player.getUniqueId().toString());
    }

    public static void clearPlayerSession(Player player) {
        LOG(LogType.Info, "clearPlayerSession");
        if(playerSessions.containsKey(player.getUniqueId().toString())) {
            
            String sessionID = playerSessions.get(player.getUniqueId().toString());
            String characterId = sessions.get(sessionID).getId();
            
            playerSessions.remove(player.getUniqueId().toString());

            for (String playerSessionId : playerSessions.values()) 
            { 
                String playerCharacterId = sessions.get(playerSessionId).getId();
                if(playerCharacterId.equals(characterId))
                {
                    return;
                }
            }
            ai.inworld.minecraftsdk.character.Character character = CharacterService.getCharacterById(characterId);
            if (character.getAware()) {
                Villager villager = character.getVillager();
                villager.setAI(true);
                villager.setAware(true);
            }
        }
        
    }

    public static Boolean hasPlayerSession(Player player) {
        return playerSessions.containsKey(player.getUniqueId().toString());
    }

    public static void addSession(String characterId, Player player) {

            String sessionKey = player.getUniqueId().toString() + "/" + characterId;

            if (sessionKeys.containsKey(sessionKey)) {
                
                LOG(LogType.Error, "SessionService session key already exists: " + sessionKey);
                playerSessions.put(player.getUniqueId().toString(), sessionKeys.get(sessionKey));
                return;

            } else {

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        
                        try {

                            Session session = new Session(characterId, player);
                            sessions.put(session.getSessionId(), session);
                            sessionKeys.put(sessionKey, session.getSessionId());
                            playerSessions.put(player.getUniqueId().toString(), session.getSessionId());
                            APIService.message(getPlayerSessionId(player), "Hello");
                            // LOG(LogType.Info, "SessionService session created: " + sessionKey + " " + session.getSessionId());
                                            
                        } catch ( ConnectException e ) {
                            LOG(LogType.Error, "SessionService ConnectException: " + e.getMessage());
                        } catch ( IOException e ) {
                            LOG(LogType.Error, "SessionService IOException: " + e.getMessage());
                        } catch( RuntimeException e ) {
                            LOG(LogType.Error, "SessionService RuntimeException: " + e.getMessage());
                        }
                        
                    }
                }.runTaskAsynchronously(plugin);

            }



    }
    
    public static void removeSession(String sessionId) {
        
        // // TODO Close all active sessions

        // Character character = characters.get(id);
        // if ( character != null ) {
        //     character.remove();
        //     characters.remove(id);
        // }
            
        try {
            
            Session session = sessions.get(sessionId);

            if (session == null ) {
                LOG(LogType.Error, "SessionService session not found with id: " + sessionId);
                return;
            }

            session.close();

        } catch ( ConnectException e ) {
            LOG(LogType.Error, "SessionService ConnectException: " + e.getMessage());
        } catch ( IOException e ) {
            LOG(LogType.Error, "SessionService IOException: " + e.getMessage());
        } catch( RuntimeException e ) {
            LOG(LogType.Error, "SessionService RuntimeException: " + e.getMessage());
        }

    } 

    public static void sendMessage(Player player, String message) throws RuntimeException {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    APIService.message(getPlayerSessionId(player), message);
                } catch ( ConnectException e ) {
                    LOG(LogType.Error, "SessionService ConnectException: " + e.getMessage());
                    throw new RuntimeException("Error sending message");
                } catch ( IOException e ) {
                    LOG(LogType.Error, "SessionService IOException: " + e.getMessage());
                    throw new RuntimeException("Error sending message");
                } catch( RuntimeException e ) {
                    LOG(LogType.Error, "SessionService RuntimeException: " + e.getMessage());
                    throw new RuntimeException("Error sending message");
                }

            }
        }.runTaskAsynchronously(plugin);
    }

}
