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

public class Session {
        
    private final String characterId;
    private final String displayName;
    private final String id;
    private final Player player;
    private final String sceneId;
    private String sessionId;

    public Session(String id, Player player) throws ConnectException, IOException, RuntimeException {

        try {

            this.id = id;
            this.player = player;

            Object idCheck = ConfigService.getConfig().get("server.characters." + this.id);

            if (idCheck == null) {
                throw new RuntimeException("Character id \"" + this.id  + "\" doesn't exists in the system");
            }

            String[] sceneParts = this.id.split("\\/");

            if (sceneParts.length != 2) {
                throw new RuntimeException("Malformed character id \"" + this.id  + "\"");
            }

            String sceneName = sceneParts[0];
            String characterName = sceneParts[1];

            Object sceneCheck = ConfigService.getConfig().get("server.scenes." + sceneName);

            if (sceneCheck == null) {
                throw new RuntimeException("Scene \"" + sceneName  + "\" doesn't exists");
            }

            this.sceneId = ConfigService.getConfig().getString("server.scenes." + sceneName + ".id");

            Object characterCheck = ConfigService.getConfig().get("server.scenes." + sceneName + ".characters." + characterName);

            if (characterCheck == null) {
                throw new RuntimeException("Character \"" + characterName  + "\" doesn't exists");
            }

            this.characterId = ConfigService.getConfig().getString("server.scenes." + sceneName + ".characters." + characterName + ".resourceName");
            this.displayName = ConfigService.getConfig().getString("server.scenes." + sceneName + ".characters." + characterName + ".displayName");

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

    public String getCharacterId() 
    {
        return this.characterId;
    }

    public String getDisplayName() 
    {
        return this.displayName;
    }

    public String getId() {
        return this.id;
    }

    public Player getPlayer() 
    {
        return this.player;
    }

    public String getPlayerName() 
    {
        return this.player.getDisplayName();
    }

    public UUID getPlayerId() 
    {
        return this.player.getUniqueId();
    }

    public String getSceneId() 
    {
        return this.sceneId;
    }

    public String getSessionId() 
    {
        return this.sessionId;
    }

    public void close() throws ConnectException, IOException, RuntimeException {

        try {

            if (this.sessionId == null ) {
                throw new RuntimeException("Error session not open");
            }

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

    private void open() throws ConnectException, IOException, RuntimeException {
        
        try {

            JSONObject response = APIService.open(this.getPlayerId().toString(), this.getSceneId(), this.getCharacterId(), this.getPlayerName());

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
