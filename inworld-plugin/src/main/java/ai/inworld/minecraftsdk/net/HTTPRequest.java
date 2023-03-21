package ai.inworld.minecraftsdk.net;


import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;


public class HTTPRequest {

    private static String errorIOException = "TODO: Add better error message for IOException";
    private static String errorMalformedURL = "TODO: Add better error message for MalformedURL";
    private static String errorProtocolException = "TODO: Add better error message for ProtocolException";
    private static String errorPost = "Couldn't submit a POST request ";
    private static String errorReadResponse = "Couldn't read a response ";

    public static String POST(String urlString, String inputString) throws ConnectException, IOException, RuntimeException { 
        
        try {
        
            return httpRequest(urlString, "POST", true, inputString);
        
        } catch (ConnectException e) {
                
            throw e;

        } catch (IOException e) {
            
            throw e;

        } catch(RuntimeException e) {
        
            throw e;
        
        }
    }

    public static String GET(String urlString) throws ConnectException, IOException, RuntimeException {
        
        try {
        
            return httpRequest(urlString, "GET", true, null);
        
        } catch (ConnectException e) {
                
            LOG(LogType.Error, e.getClass().toString());
            throw e;

        } catch (IOException e) {
            
            LOG(LogType.Error, e.getClass().toString());
            throw e;

        } catch(RuntimeException e) {
        
            throw e;
        
        }

    }

    public static String httpRequest(String urlString, String requestMethod, boolean doOutput, String inputString) throws ConnectException, IOException, RuntimeException {

        URL url = null;

        try {

            url = new URL(urlString);
        
        } catch (MalformedURLException e) {
        
            LOG(LogType.Error, errorMalformedURL);
            throw new RuntimeException(e);
        
        }

        HttpURLConnection con = null;

        try {

            con = (HttpURLConnection) url.openConnection();
        
        } catch (IOException e) {
        
            LOG(LogType.Error, errorIOException);
            throw e;
        
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

            try {
                
                OutputStream os = con.getOutputStream();

                byte[] input = inputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);

            } catch (ConnectException e) {
                throw e;
            } catch (IOException e) {
                throw e;
            } catch (RuntimeException e) {
                throw e;
            }

        }

        try {
           
            if (con.getResponseCode() > 299) {
                throw new RuntimeException("Response Error: " + Integer.toString(con.getResponseCode()) + ": "  + con.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder response = new StringBuilder();
            String responseLine = null;

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return response.toString();

        } catch (IOException e) {

            LOG(LogType.Error, e.getClass() + " " + e.getMessage() + " " + errorReadResponse + urlString);
            throw new RuntimeException(e);

        } catch (RuntimeException e) {
            throw e;
        }

    }

}
