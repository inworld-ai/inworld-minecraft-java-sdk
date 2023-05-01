package ai.inworld.minecraftsdk.services;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import ai.inworld.minecraftsdk.session.Session;

import static ai.inworld.minecraftsdk.net.HTTPRequest.GET;
import static ai.inworld.minecraftsdk.net.HTTPRequest.POST;
import static ai.inworld.minecraftsdk.net.HTTPRequest.EncodeValue;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This service class handles the Inworld REST API calls. If it's a POST call it builds the stringified JSON object
 */
public final class APIService {
    
    /**
     * This opens an Inworld session
     * @param sceneId The Inworld Scene Id for the character
     * @return JSONObject The response object containing the Inworld session Id and character information
     * @throws ConnectException Thrown if there was a connection error
     * @throws IOException Thrown if there was an error in the data
     * @throws RuntimeException Thrown for all other errors
     */
    public static JSONObject open(String sceneId) throws ConnectException, IOException, RuntimeException {
       
        try {

            // Create the JSON object
            JSONObject data = new JSONObject();
            data.put("name", sceneId);
            
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("authorization", getAuthHeader());
            headers.put("Grpc-Metadata-session-id", "inworld_wonderland_roblox:00000000-00000000-00000000-00000000");
    
            // Stringify the JSON object and send the request to the Inworld REST service
            String jsonString = POST(APIService.getAPIHost() + sceneId + ":openSession", data.toJSONString(), headers);
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

    // /**
    //  * This closes an Inworld session
    //  * @param sessionId The session id to close
    //  * @throws ConnectException Thrown if there was a connection error
    //  * @throws IOException Thrown if there was an error in the data
    //  * @throws RuntimeException Thrown for all other errors
    //  */
    // public static void close(String sessionId) throws ConnectException, IOException, RuntimeException {
        
    //     try {
            
    //         // Sends the close request to the Inworld REST service
    //         GET(APIService.getAPIHost() + "/session/" + sessionId + "/close");
        
    //     } catch ( ConnectException e) {
    //         throw new ConnectException("Unable to connect to API Host: " + getAPIHost());
    //     } catch ( IOException e) {
    //         throw e;
    //     } catch ( RuntimeException e) {
    //         throw e;
    //     }
        
    // }

    // /**
    //  * This closes all open Inworld sessions by a Minecraft Player's UID
    //  * @param playerId The Minecraft Player's UID
    //  * @throws ConnectException Thrown if there was a connection error
    //  * @throws IOException Thrown if there was an error in the data
    //  * @throws RuntimeException Thrown for all other errors
    //  */
    // public static void closeAllByPlayerId(String playerId) throws ConnectException, IOException, RuntimeException {
        
    //     try {
            
    //         // Sends the close request to the Inworld REST service
    //         GET(APIService.getAPIHost() + "/session/closeall/" + playerId + "/server/" + ServerService.SERVER_ID);
        
    //     } catch ( ConnectException e) {
    //         throw new ConnectException("Unable to connect to API Host: " + getAPIHost());
    //     } catch ( IOException e) {
    //         // throw e;
    //     } catch ( RuntimeException e) {
    //         LOG(LogType.Error, "APIServer closeAllByPlayerId " + e.getMessage());
    //         // throw e;
    //     }
        
    // }

    // /**
    //  * This retrieves any events on the Inworld REST service
    //  * @return ArrayList<JSONObject> A list of events from the Inworld REST service
    //  * @throws ConnectException Thrown if there was a connection error
    //  * @throws IOException Thrown if there was an error in the data
    //  * @throws RuntimeException Thrown for all other errors
    //  */
    // public static ArrayList<JSONObject> getEvents() throws ConnectException, IOException, RuntimeException {

    //     try {
            
    //         // Sends the request to the Inworld REST service to retrieve all events
    //         String jsonString = GET(APIService.getAPIHost() + "/events");
    //         if (jsonString == null) {
    //             return null;
    //         }

    //         // Processes the string response into an array of JSONObjects
    //         ArrayList<JSONObject> jsonObjects = (ArrayList<JSONObject>) JSONValue.parse(jsonString);
    //         return jsonObjects;

    //     } catch ( ConnectException e) {
    //         throw new ConnectException("Unable to connect to Inworld REST API Host");
    //     } catch ( IOException e) {
    //         throw e;
    //     } catch ( RuntimeException e) {
    //         LOG(LogType.Error, "APIServer getEvents " + e.getMessage());
    //         throw e;
    //     }

    // }

    /**
     * This sends a message to an active session
     * @param session The player's active session
     * @param player The player sending the message
     * @param message The message to send
     * @throws ConnectException Thrown if there was a connection error
     * @throws IOException Thrown if there was an error in the data
     * @throws RuntimeException Thrown for all other errors
     */
    public static void message(Session session, Player player, String message) throws ConnectException, IOException, RuntimeException {
        
        try {
            
            // Create the URL parameters
            Map<String, String> data = new HashMap<String, String>();
            data.put("character", session.getId());
            data.put("sessionId", session.getSessionId());
            data.put("characterId", session.getInstanceId());
            data.put("text", EncodeValue(message));
            data.put("endUserFullname", player.getDisplayName());
            data.put("endUserId", player.getUniqueId().toString());
            
            // Encode the URL parameters
            String urlParams = data.keySet().stream().map(key -> key + "=" + data.get(key)).collect(Collectors.joining("&"));

            // Build the HTTP Request headers
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("authorization", getAuthHeader());
            headers.put("Grpc-Metadata-session-id", "inworld_wonderland_roblox:00000000-00000000-00000000-00000000");

            // Sends the message to the service
            String jsonString = GET(APIService.getAPIHost() + session.getCharacterId() + ":simpleSendText?" + urlParams, headers);
            if (jsonString == null) {
                throw new RuntimeException("No response for sending message.");
            }

            // Converts the returned data into a JSON object
            JSONObject result = (JSONObject) JSONValue.parse(jsonString);
            if (!result.containsKey("textList")) {
                throw new RuntimeException("No text messages in response.");
            }

            String text = "";
            JSONArray raw = (JSONArray) result.get("textList");
            for (int i=0; i < raw.size(); i++ ) {
                text += raw.get(i);
            }
            session.getPlayer().sendMessage(ChatColor.RED + session.getDisplayName() + ": " + ChatColor.GRAY + "» " + ChatColor.WHITE + text);

            return;
        
        } catch ( ConnectException e) {
            throw new ConnectException("Unable to connect to API Host: " + getAPIHost());
        } catch ( IOException e) {
            throw e;
        } catch ( RuntimeException e) {
            throw e;
        }
        
    }

    /**
     * Generates the Base64 Authentication Header
     * @return String The Base64 authentication header
     * @throws RuntimeException
     */
    public static String getAuthHeader() {
        String apiKey = ConfigService.getConfig().getString("server.api.key");
        String apiSecret = ConfigService.getConfig().getString("server.api.secret");
        return "Basic " + Base64.getEncoder().encodeToString((apiKey + ":" + apiSecret).getBytes());
    }

    /**
     * Helper function to determine which host to send to depending on the Minecraft server 
     * running on localhost vs a remote service
     * @return String The host to use for the GET/POST requests
     */
    public static String getAPIHost() throws RuntimeException {
        return "https://studio.inworld.ai/v1/";
    }

}
