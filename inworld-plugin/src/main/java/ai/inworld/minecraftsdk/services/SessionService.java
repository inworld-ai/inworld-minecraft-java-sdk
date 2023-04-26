package ai.inworld.minecraftsdk.services;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import ai.inworld.minecraftsdk.session.Session;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

/**
 * This service class manages all sessions open and active. 
 * When a session is open it is maintained until closure
 * When a session is active it has a Player currently chatting with a character
 */
public class SessionService {
    
    // A collection maintaining active sessions
    // Structure is [Player UID | Session ID]
    private static final HashMap<String, String> playerSessions = new HashMap<>();
    // A collection of open Sessions 
    // Structure is [Session ID | Session]
    private static final HashMap<String, Session> sessions = new HashMap<>();
    // A reference collection of open sessions by a unique key
    // Structure is [SessionKey | Session ID]
    private static final HashMap<String, String> sessionKeys = new HashMap<>();

    private static JavaPlugin plugin;

    /**
     * This method is called upon enabling the plugin.
     * @param plugin A reference to the Minecraft plugin
     */
    public SessionService(JavaPlugin plugin) {

        LOG(LogType.Info, "SessionService init");
        SessionService.plugin = plugin;

    }

    /**
     * This method is called to open a session and also to set an open session active
     * @param characterId The internal character id
     * @param player The Player opening the session
     */
    public static void addSession(String characterId, Player player) throws RuntimeException {

        String sessionKey = player.getUniqueId().toString() + "/" + characterId;
        
        // Checks if a sessions is already open
        if (sessionKeys.containsKey(sessionKey)) {
            
            // Sets the open session to active
            LOG(LogType.Error, "SessionService session key already exists: " + sessionKey);
            playerSessions.put(player.getUniqueId().toString(), sessionKeys.get(sessionKey));
            return;

        } else {

            // Create a thread to open a new session to prevent blocking the plugin's main process thread
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
                        player.sendMessage(ChatColor.RED + "Error" + ": " + ChatColor.GRAY + "» " + ChatColor.WHITE + "There was an error starting the chat");
                    } catch ( IOException e ) {
                        LOG(LogType.Error, "SessionService IOException: " + e.getMessage());
                        player.sendMessage(ChatColor.RED + "Error" + ": " + ChatColor.GRAY + "» " + ChatColor.WHITE + "There was an error starting the chat");
                    } catch( RuntimeException e ) {
                        LOG(LogType.Error, "SessionService RuntimeException: " + e.getMessage());
                        player.sendMessage(ChatColor.RED + "Error" + ": " + ChatColor.GRAY + "» " + ChatColor.WHITE + "There was an error starting the chat");
                    }
                    
                }
            }.runTaskAsynchronously(plugin);

        }

    }

    /**
     * This function is used to remove a player from the active character session list.
     * It checks if any other player is chatting with the same character and if not
     * it checks if the character is set to have AI and if so it renables it.
     * @param player The player to clear the active session
     */
    public static void clearPlayerSession(Player player) {
        
        LOG(LogType.Info, "clearPlayerSession");

        // Checks if the player has an active session
        if(playerSessions.containsKey(player.getUniqueId().toString())) {
            
            String sessionID = playerSessions.get(player.getUniqueId().toString());
            String characterId = sessions.get(sessionID).getId();
            
            // Remove the active session
            playerSessions.remove(player.getUniqueId().toString());

            // Check if any other players are speaking to the same Character
            for (String playerSessionId : playerSessions.values()) 
            { 
                String playerCharacterId = sessions.get(playerSessionId).getId();
                if(playerCharacterId.equals(characterId))
                {
                    return;
                }
            }

            // If no other player is speaking to the Character then check if the character
            // is set for aware. If so then renable aware and ai for the Villager entity.
            ai.inworld.minecraftsdk.character.Character character = CharacterService.getCharacterById(characterId);            
            if (character.getAware()) {
                Villager villager = character.getVillager();
                villager.setAI(true);
                villager.setAware(true);
            }

        }
        
    }

    /**
     * This method closes all the sessions open by the Player
     * @param player The player to close the sessions for
     */
    // public static void closeAllSessionByPlayer(Player player) {
    //     try {
    //         APIService.closeAllByPlayerId(player.getUniqueId().toString());
    //     } catch ( ConnectException e ) {
    //         LOG(LogType.Error, "SessionService ConnectException: " + e.getMessage());
    //     } catch ( IOException e ) {
    //         LOG(LogType.Error, "SessionService IOException: " + e.getMessage());
    //     } catch( RuntimeException e ) {
    //         LOG(LogType.Error, "SessionService RuntimeException: " + e.getMessage());
    //     }
    // }

    /**
     * This method returns a session by session id
     * @param sessionId The session id
     * @return Session The session
     */
    public static Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * This method returns a Players active session id
     * @param player A Player
     * @return String The active session id
     */
    public static String getPlayerSessionId(Player player) {
        return playerSessions.get(player.getUniqueId().toString());
    }

    /** 
     * This method checks if a Player has an active session
     * @return Boolean If the player has an active session
     */
    public static Boolean hasPlayerSession(Player player) {
        return playerSessions.containsKey(player.getUniqueId().toString());
    }

    /**
     * This method removes a Player from this service. It's used when a Player quits the server.
     * @param player The Player to remove
     */
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

        // new BukkitRunnable() {
        //     @Override
        //     public void run() {
        //         closeAllSessionByPlayer(player);
        //     }
        // }.runTaskAsynchronously(plugin);

    }
    
    /**
     * This method is not currently used 
     * @param sessionId The session to remove
     */
    public static void removeSession(String sessionId) {
            
        try {
            
            Session session = sessions.get(sessionId);

            if (session == null ) {
                LOG(LogType.Error, "SessionService session not found with id: " + sessionId);
                return;
            }

            // session.close();

            String sessionKey = session.getPlayerId().toString() + "/" + session.getId();

            sessionKeys.remove(sessionKey);
            sessions.remove(sessionId);


        } catch( RuntimeException e ) {
            LOG(LogType.Error, "SessionService RuntimeException: " + e.getMessage());
        }

    } 

    /**
     * This method handles receiving a message sent from chat and sending to the API Service
     * @param player The player sending the message
     * @param message The message to send
     * @throws RuntimeException Throws if there are any errors sending the message
     */
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
