package ai.inworld.minecraftsdk.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


/**
 * This class is helper functions for processing JSON objects
 */
public class JSONUtils {
    
    /**
     * This method accepts a string representing an array of JSON objects and 
     * parses it into JSON objects
     * @param jsonString
     * @return ArrayList<JSONObject> An array of JSON objects
     */
    public static ArrayList<JSONObject> toJSON(String jsonString) {

        if (jsonString == null) {
            return null;
        }

        jsonString = jsonString.replace("[","").replace("]","");
        ArrayList<String> jsonStrings = new ArrayList<>(List.of(jsonString.split("},")));
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();

        for (String json : jsonStrings) {
            if (json == null || json.length() == 0 || json.charAt(0) != '{') {
                break;
            }
            jsonObjects.add(new JSONObject(json + "}"));
        }

        return jsonObjects;
        
    }
}
