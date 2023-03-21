package ai.inworld.minecraftsdk.services;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import static ai.inworld.minecraftsdk.net.HTTPRequest.GET;
import static ai.inworld.minecraftsdk.net.HTTPRequest.POST;
import static ai.inworld.minecraftsdk.services.ServerService.SERVER_ID;
import static ai.inworld.minecraftsdk.services.ServerService.SERVER_IP;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import java.io.IOException;
import java.net.ConnectException;

public final class APIService {
    
    public static JSONObject open(String uid, String sceneId, String characterId, String playerName) throws ConnectException, IOException, RuntimeException {
       
        try {

            JSONObject data = new JSONObject();
            data.put("uid", uid);
            data.put("sceneId", sceneId);
            data.put("characterId", characterId);
            data.put("playerName", playerName);
            data.put("serverId", SERVER_ID);
            
            // LOG(LogType.Info, data.toJSONString());
    
            String jsonString = POST(getAPIHost() + "/session/open", data.toJSONString());
            if (jsonString == null) {
                return null;
            }
    
            JSONObject result = (JSONObject) JSONValue.parse(jsonString);
    
            return result;

        } catch ( ConnectException e) {
            throw new ConnectException("Unable to connect to API Host: " + getAPIHost());
        } catch ( IOException e) {
            throw e;
        } catch ( RuntimeException e) {
            throw e;
        }

    }

    public static void close(String sessionId) throws ConnectException, IOException, RuntimeException {
        
        try {
        
            GET(getAPIHost() + "/session/" + sessionId + "/close");
        
        } catch ( ConnectException e) {
            throw new ConnectException("Unable to connect to API Host: " + getAPIHost());
        } catch ( IOException e) {
            throw e;
        } catch ( RuntimeException e) {
            throw e;
        }
        
    }

    private static String getAPIHost() {

        if (SERVER_IP.equals("localhost")) {
            return ConfigService.getConfig().getString("server.api.dev");
        } else {
            return ConfigService.getConfig().getString("server.api.prod");
        }

    }

}