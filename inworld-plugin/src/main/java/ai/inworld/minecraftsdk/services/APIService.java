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
import java.util.ArrayList;

/**
 * This service class handles the Inworld REST API calls. If it's a POST call it builds the stringified JSON object
 */
public final class APIService {
    
    /**
     * This opens an Inworld session
     * @param uid The Minecraft Player's UID
     * @param sceneId The Inworld Scene Id for the character
     * @param characterId The Inworld Character Id
     * @param playerName The display name of the Minecraft Player
     * @return JSONObject The response object containing the Inworld session Id and character information
     * @throws ConnectException Thrown if there was a connection error
     * @throws IOException Thrown if there was an error in the data
     * @throws RuntimeException Thrown for all other errors
     */
    public static JSONObject open(String uid, String sceneId, String characterId, String playerName) throws ConnectException, IOException, RuntimeException {
       
        try {

            // Create the JSON object
            JSONObject data = new JSONObject();
            data.put("uid", uid);
            data.put("sceneId", sceneId);
            data.put("characterId", characterId);
            data.put("playerName", playerName);
            data.put("serverId", SERVER_ID);
            
            // LOG(LogType.Info, data.toJSONString());
    
            // Stringify the JSON object and send the request to the Inworld REST service
            String jsonString = POST(APIService.getAPIHost() + "/session/open", data.toJSONString());
            if (jsonString == null) {
                return null;
            }
            
            // Converts the returned data into a JSON object
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

    /**
     * This closes an Inworld session
     * @param sessionId The session id to close
     * @throws ConnectException Thrown if there was a connection error
     * @throws IOException Thrown if there was an error in the data
     * @throws RuntimeException Thrown for all other errors
     */
    public static void close(String sessionId) throws ConnectException, IOException, RuntimeException {
        
        try {
            
            // Sends the close request to the Inworld REST service
            GET(APIService.getAPIHost() + "/session/" + sessionId + "/close");
        
        } catch ( ConnectException e) {
            throw new ConnectException("Unable to connect to API Host: " + getAPIHost());
        } catch ( IOException e) {
            throw e;
        } catch ( RuntimeException e) {
            throw e;
        }
        
    }

    /**
     * This closes all open Inworld sessions by a Minecraft Player's UID
     * @param playerId The Minecraft Player's UID
     * @throws ConnectException Thrown if there was a connection error
     * @throws IOException Thrown if there was an error in the data
     * @throws RuntimeException Thrown for all other errors
     */
    public static void closeAllByPlayerId(String playerId) throws ConnectException, IOException, RuntimeException {
        
        try {
            
            // Sends the close request to the Inworld REST service
            GET(APIService.getAPIHost() + "/session/closeall/" + playerId + "/server/" + ServerService.SERVER_ID);
        
        } catch ( ConnectException e) {
            throw new ConnectException("Unable to connect to API Host: " + getAPIHost());
        } catch ( IOException e) {
            // throw e;
        } catch ( RuntimeException e) {
            LOG(LogType.Error, "APIServer closeAllByPlayerId " + e.getMessage());
            // throw e;
        }
        
    }

    /**
     * This retrieves any events on the Inworld REST service
     * @return ArrayList<JSONObject> A list of events from the Inworld REST service
     * @throws ConnectException Thrown if there was a connection error
     * @throws IOException Thrown if there was an error in the data
     * @throws RuntimeException Thrown for all other errors
     */
    public static ArrayList<JSONObject> getEvents() throws ConnectException, IOException, RuntimeException {

        try {
            
            // Sends the request to the Inworld REST service to retrieve all events
            String jsonString = GET(APIService.getAPIHost() + "/events");
            if (jsonString == null) {
                return null;
            }

            // Processes the string response into an array of JSONObjects
            ArrayList<JSONObject> jsonObjects = (ArrayList<JSONObject>) JSONValue.parse(jsonString);
            return jsonObjects;

        } catch ( ConnectException e) {
            throw new ConnectException("Unable to connect to Inworld REST API Host");
        } catch ( IOException e) {
            throw e;
        } catch ( RuntimeException e) {
            LOG(LogType.Error, "APIServer getEvents " + e.getMessage());
            throw e;
        }

    }

    /**
     * This sends a message to an active session
     * @param sessionId The Inworld session id
     * @param message The message to send
     * @throws ConnectException Thrown if there was a connection error
     * @throws IOException Thrown if there was an error in the data
     * @throws RuntimeException Thrown for all other errors
     */
    public static void message(String sessionId, String message) throws ConnectException, IOException, RuntimeException {
        
        try {
            
            // Create the JSON object
            JSONObject data = new JSONObject();
            data.put("message", message);
            
            // Sends the message to the service
            POST(APIService.getAPIHost() + "/session/" + sessionId + "/message", data.toJSONString());
        
        } catch ( ConnectException e) {
            throw new ConnectException("Unable to connect to API Host: " + getAPIHost());
        } catch ( IOException e) {
            throw e;
        } catch ( RuntimeException e) {
            throw e;
        }
        
    }

    /**
     * Helper function to determine which host to send to depending on the Minecraft server 
     * running on localhost vs a remote service
     * @return String The host to use for the GET/POST requests
     */
    public static String getAPIHost() throws RuntimeException {

        String host = "";

        if (SERVER_IP.equals("localhost")) {
            host = ConfigService.getConfig().getString("server.api.dev");
        } else {
            host = ConfigService.getConfig().getString("server.api.prod");
        }

        if (host.equals("")) {
            throw new RuntimeException("Host is not defined");
        }

        return host;

    }

}
