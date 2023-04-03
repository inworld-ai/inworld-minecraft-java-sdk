package ai.inworld.minecraftsdk.session;

import ai.inworld.minecraftsdk.services.APIService;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.MessageService;

import java.io.IOException;
import java.net.ConnectException;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

/**
 * This class defines creation and closing an Inworld Session
 */
public class Session {
        
    // The internal character id
    private final String characterId;
    // The display name of the character/villager
    private final String displayName;
    // The Inworld character id
    private final String id;
    // The Player associated with the session
    private final Player player;
    // The Inworld scene id
    private final String sceneId;
    // The session id
    private String sessionId;

    /**
     * This method creates a Session
     * @param id This Inworld Session id
     * @param player The player of the session
     * @throws ConnectException Throw if there's an error opening the connection
     * @throws IOException Throw if there's an error in data structure
     * @throws RuntimeException Thrown if there's any other errors
     */
    public Session(String id, Player player) throws ConnectException, IOException, RuntimeException {

        try {

            this.id = id;
            this.player = player;

            // Checks if the character has been added in the game
            Object idCheck = ConfigService.getConfig().get("server.characters." + this.id);
            if (idCheck == null) {
                throw new RuntimeException("Character id \"" + this.id  + "\" doesn't exists in the system");
            }

            // Splits the Inworld Character id into parts
            String[] sceneParts = this.id.split("\\/");
            if (sceneParts.length != 2) {
                throw new RuntimeException("Malformed character id \"" + this.id  + "\"");
            }

            String sceneName = sceneParts[0];
            String characterName = sceneParts[1];

            // Checks if the scene exists in the configuration
            Object sceneCheck = ConfigService.getConfig().get("server.scenes." + sceneName);
            if (sceneCheck == null) {
                throw new RuntimeException("Scene \"" + sceneName  + "\" doesn't exists");
            }

            this.sceneId = ConfigService.getConfig().getString("server.scenes." + sceneName + ".id");

            // Checks if the character exists in the configuration
            Object characterCheck = ConfigService.getConfig().get("server.scenes." + sceneName + ".characters." + characterName);
            if (characterCheck == null) {
                throw new RuntimeException("Character \"" + characterName  + "\" doesn't exists");
            }

            this.characterId = ConfigService.getConfig().getString("server.scenes." + sceneName + ".characters." + characterName + ".resourceName");
            this.displayName = ConfigService.getConfig().getString("server.scenes." + sceneName + ".characters." + characterName + ".displayName");

            // Opens the session
            this.open();

            return;
                   
        } catch ( ConnectException e ) {
            throw e;
        } catch ( IOException e ) {
            throw e;
        } catch( RuntimeException e ) {
            throw e;
        }

    }

    /**
     * @return String the internal character id
     */
    public String getCharacterId() 
    {
        return this.characterId;
    }

    /**
     * @return String the display name of the character
     */
    public String getDisplayName() 
    {
        return this.displayName;
    }

    /**
     * @return The Inworld character id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return The session's Player
     */
    public Player getPlayer() 
    {
        return this.player;
    }

    /**
     * @return The Player name of the session
     */
    public String getPlayerName() 
    {
        return this.player.getDisplayName();
    }

    /**
     * @return The UUID of the session's Player
     */
    public UUID getPlayerId() 
    {
        return this.player.getUniqueId();
    }

    /**
     * @return The Inworld scene id
     */
    public String getSceneId() 
    {
        return this.sceneId;
    }

    /**
     * @return The Inworld session id
     */
    public String getSessionId() 
    {
        return this.sessionId;
    }

    /**
     * This method closes the session
     * @throws ConnectException Throw if there's an error closes the connection
     * @throws IOException Throw if there's an error in data structure
     * @throws RuntimeException Thrown if there's any other errors
     */
    public void close() throws ConnectException, IOException, RuntimeException {

        try {

            if (this.sessionId == null ) {
                throw new RuntimeException("Error session not open");
            }

            // Close the session
            APIService.close(this.sessionId);

        } catch ( ConnectException e ) {
            throw e;
        } catch ( IOException e ) {
            throw e;
        } catch( RuntimeException e ) {
            throw e;
        }

        return;

    }

    /**
     * This method opens the session
     * @throws ConnectException Throw if there's an error open the connection
     * @throws IOException Throw if there's an error in data structure
     * @throws RuntimeException Thrown if there's any other errors
     */
    private void open() throws ConnectException, IOException, RuntimeException {
        
        try {
            
            // Open a session
            JSONObject response = APIService.open(this.getPlayerId().toString(), this.getSceneId(), this.getCharacterId(), this.getPlayerName());

            // Check that the session was created correctly
            if (!response.containsKey("character")) {
                throw new RuntimeException("Error SceneId doesn't exist: " + this.getSceneId());
            }
            
            if (!response.containsKey("sessionId")) {
                throw new RuntimeException("Error unable to start session");
            }
        
            this.sessionId = response.get("sessionId").toString();
                    
        } catch ( ConnectException e ) {
            throw e;
        } catch ( IOException e ) {
            throw e;
        } catch( RuntimeException e ) {
            throw e;
        }

        return;

    }

}
