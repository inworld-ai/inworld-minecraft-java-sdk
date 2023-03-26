package ai.inworld.minecraftsdk.services;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Set;

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

        LOG(LogType.Info, "SessionService init");
        SessionService.plugin = plugin;

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

    // This function is used to remove a player from the active character session list.
    // It checks if any other player is chatting with the same character and if not
    // it checks if the character is set to have AI and renables it.
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

    public static void closeAllSessionByPlayer(Player player) {
        try {
            APIService.closeAllByPlayerId(player.getUniqueId().toString());
        } catch ( ConnectException e ) {
            LOG(LogType.Error, "SessionService ConnectException: " + e.getMessage());
        } catch ( IOException e ) {
            LOG(LogType.Error, "SessionService IOException: " + e.getMessage());
        } catch( RuntimeException e ) {
            LOG(LogType.Error, "SessionService RuntimeException: " + e.getMessage());
        }
    }

    public static Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public static String getPlayerSessionId(Player player) {
        return playerSessions.get(player.getUniqueId().toString());
    }

    public static Boolean hasPlayerSession(Player player) {
        return playerSessions.containsKey(player.getUniqueId().toString());
    }

    public static void removePlayer(Player player) {

        SessionService.clearPlayerSession(player);

        String[] sessionKeysToParse = sessions.keySet().toArray(new String[sessions.size()]);

        for( String sessionId: sessionKeysToParse ) {

            Session session = sessions.get(sessionId);

            if ( session.getPlayerId() == player.getUniqueId() ) {

                LOG(LogType.Info, "SessionService removePlayer " + session.getId());

                String sessionKey = session.getPlayerId().toString() + "/" + session.getId();

                if (!sessionKeys.containsKey(sessionKey))
                {
                    throw new RuntimeException("Error SessionService removePlayer session does not exist in sessionKeys");
                }

                sessionKeys.remove(sessionKey);
                sessions.remove(sessionId);

            }

        }

        new BukkitRunnable() {
            @Override
            public void run() {
                closeAllSessionByPlayer(player);
            }
        }.runTaskAsynchronously(plugin);

    }
    
    // TODO not currently used
    public static void removeSession(String sessionId) {
            
        try {
            
            Session session = sessions.get(sessionId);

            if (session == null ) {
                LOG(LogType.Error, "SessionService session not found with id: " + sessionId);
                return;
            }

            session.close();

            String sessionKey = session.getPlayerId().toString() + "/" + session.getId();

            sessionKeys.remove(sessionKey);
            sessions.remove(sessionId);


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
