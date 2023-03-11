package ai.inworld.minecraftsdk.net;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ai.inworld.minecraftsdk.utils.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.Logger.LogType;


public class HTTPRequest {

    private static String errorIOException = "TODO: Add better error message for IOException";
    private static String errorMalformedURL = "TODO: Add better error message for MalformedURL";
    private static String errorProtocolException = "TODO: Add better error message for ProtocolException";
    private static String errorPost = "Couldn't submit a POST request ";
    private static String errorReadResponse = "Couldn't read a response ";

    public static String POST(String urlString, String inputString) {
        return httpRequest(urlString, "POST", true, inputString);
    }

    public static ArrayList<JSONObject> GET(String urlString) {
        String jsonString = httpRequest(urlString, "GET", true, null);
        return stringToJsonArray(jsonString);
    }

    public static String httpRequest(String urlString, String requestMethod, boolean doOutput, String inputString) {

        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            LOG(LogType.Error, errorMalformedURL);
            throw new RuntimeException(e);
        }

        HttpURLConnection con = null;

        try {
            con = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            LOG(LogType.Error, errorIOException + urlString);
            throw new RuntimeException(e);
        }

        try {
            con.setRequestMethod(requestMethod);
        } catch (ProtocolException e) {
            LOG(LogType.Error, errorProtocolException);
            throw new RuntimeException(e);
        }

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(doOutput);

        if (requestMethod.equals("POST")) {

            try(OutputStream os = con.getOutputStream()) {

                byte[] input = inputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);

            } catch (IOException e) {

                LOG(LogType.Error, errorPost + urlString);
                return null;

            }

        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            
            StringBuilder response = new StringBuilder();
            String responseLine = null;

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return response.toString();

        } catch (IOException e) {
            LOG(LogType.Error, errorReadResponse + urlString);
            throw new RuntimeException(e);
        }

    }

    private static ArrayList<JSONObject> stringToJsonArray(String jsonString) {

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
