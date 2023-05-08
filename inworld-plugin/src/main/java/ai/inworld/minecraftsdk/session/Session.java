package ai.inworld.minecraftsdk.session;

import ai.inworld.minecraftsdk.services.APIService;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.MessageService;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

/**
 * This class defines creation and closing an Inworld Session
 */
public class Session {
        
    // The Inworld character id
    private final String characterId;
    // The Inworld character name
    private final String characterName;
    // The display name of the character/villager
    private final String displayName;
    // The internal character id
    private final String id;
    // The unqiue per session per character instance id
    private String instanceId;
    // The Player associated with the session
    private final Player player;
    // The Inworld scene id
    private final String sceneId;
    // The Inworld scene name
    private final String sceneName;
    // The session id
    private String sessionId;
    // The Inworld workspace name
    private final String workspaceName;

    /**
     * This method creates a Session
     * @param id The internal character ids
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

            this.sceneName = sceneParts[0];
            this.characterName = sceneParts[1];

            // Checks if the scene exists in the configuration
            Object sceneCheck = ConfigService.getConfig().get("server.scenes." +  this.sceneName);
            if (sceneCheck == null) {
                throw new RuntimeException("Scene \"" +  this.sceneName  + "\" doesn't exists");
            }

            this.sceneId = ConfigService.getConfig().getString("server.scenes." +  this.sceneName + ".id");

            // Splits the Inworld Character id into parts
            String[] sceneIdParts = this.sceneId.split("\\/");
            if (sceneIdParts.length != 4) {
                throw new RuntimeException("Malformed scene id \"" + this.sceneId  + "\"");
            }
            
            this.workspaceName = sceneIdParts[1];

            // Checks if the character exists in the configuration
            Object characterCheck = ConfigService.getConfig().get("server.scenes." +  this.sceneName + ".characters." + this.characterName);
            if (characterCheck == null) {
                throw new RuntimeException("Character \"" + this.characterName  + "\" doesn't exists");
            }

            this.characterId = ConfigService.getConfig().getString("server.scenes." +  this.sceneName + ".characters." + this.characterName + ".name");
            this.displayName = ConfigService.getConfig().getString("server.scenes." +  this.sceneName + ".characters." + this.characterName + ".displayName");

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
     * @return The Inworld unqiue per session and character instance id
     */
    public String getInstanceId() {
        return this.instanceId;
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
     * @return The Inworld workspace name
     */
    public String getWorkspaceName() 
    {
        return this.workspaceName;
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
            JSONObject response = APIService.open(this.getSceneId());

            // Check that the session was created correctly
            if (!response.containsKey("name")) {
                throw new RuntimeException("Error unable to add scene");
            }
            this.sessionId = response.get("name").toString();

            if (!response.containsKey("sessionCharacters")) {
                throw new RuntimeException("Error scene not found: " + sceneId);
            }

            ArrayList<HashMap<String, String>> sceneChars = (ArrayList<HashMap<String, String>>) response.get("sessionCharacters");
            for(int i=0; i < sceneChars.size(); i++) {
                HashMap<String, String> sceneChar = sceneChars.get(i);
                if( this.characterId.equals(sceneChar.get("name"))) {
                    this.instanceId = sceneChar.get("character");
                    return;
                }
            }

            throw new RuntimeException("Error character not found in scene: " + this.id);
                    
        } catch ( ConnectException e ) {
            throw e;
        } catch ( IOException e ) {
            throw e;
        } catch( RuntimeException e ) {
            throw e;
        }

    }

}
