package ai.inworld.minecraftsdk.command.commands;

import org.json.simple.JSONObject;

import ai.inworld.minecraftsdk.command.Command;
import ai.inworld.minecraftsdk.command.CommandBase;
import ai.inworld.minecraftsdk.services.APIService;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.MessageService;

import org.bukkit.entity.Player;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the /inworld scene command used in adding, removing, listing scenes and 
 * listing the characters in a scene. 
 * Also handles toggling the awareness state of the character
 * Command List:
 * - 'add' Adds an Inworld scene to the plugin
 * - 'remove' Removes and Inworld scene from the plugin
 * - 'list' Lists the current Inworld scenes
 * - 'characters' Lists a scenes characters
 * - 'update' Not currently used
 */     
public class SceneCommand extends CommandBase implements Command {
    
    public SceneCommand() {
        
        this.permission = "inworld.command.scene";
        this.description = "Adds, Removes, Lists Scenes and Lists a Scenes Characters";
        this.name = "scene";
        this.version = "1.0";
        this.syntax = "/inworld scene [command] [sceneId]";
        this.minArgs = 2;
        this.maxArgs = 3;

        final List<String> commandList = new ArrayList<>();
        commandList.add("add");
        commandList.add("remove");
        commandList.add("list");
        commandList.add("characters");
        // commandList.add("update"); Not currently used
        tabCompletes.put(2, commandList);

    }

    /**
     * Generates the tab autocomplete of the command while the Player is typing it
     * @param args The array of arguments sent
     * @param index The current index of the command 
     */
    @Override
    public List<String> getTabComplete(String[] args, int index) {

        // Generates for the 'remove', 'characters' and 'update' commands
        // Note: 'add' does not have an autocomplete generated
        if ( args.length == 2 ) {
        
            String command = args[1];
            final List<String> sceneList = new ArrayList<>();

            if ((command.equals("remove")
            || command.equals("characters")
            || command.equals("update")) 
                && ConfigService.getConfig().getConfigurationSection("server.scenes") != null) {
                for(String scene : ConfigService.getConfig().getConfigurationSection("server.scenes").getKeys(false)) {
                    sceneList.add(scene);
                }
            }
            
            tabCompletes.put(3, sceneList);

        }

        return super.getTabComplete(args, index);

    }

    /**
     * @param sender The Player that is sending the command
     * @param args The array of arguments sent
     */
    @Override
    protected void processCommand(Player sender, String[] args) {
        
        // Handles the 'list' command
        if ( args.length == 2 ) {

            String command = args[1];
            
            if (command.equals("list")) {

                if ( ConfigService.getConfig().getConfigurationSection("server.scenes") == null ) {
                    MessageService.sendPlayerMessage(sender, "- " + "There are no scenes in the game");
                    return;
                }

                MessageService.sendPlayerMessage(sender, "Scenes in system:");
                for(String scene : ConfigService.getConfig().getConfigurationSection("server.scenes").getKeys(false)) {
                    MessageService.sendPlayerMessage(sender, "- " + scene);
                }

                return;

            }

        }

        // Handles the 'remove' command
        if ( args.length == 3 ) {
            
            String command = args[1];
            String name = args[2];
            
            if (command.equals("remove")) {

                ConfigService.getConfig().set("server.scenes." + name, null);
                ConfigService.save();
                MessageService.sendPlayerMessage(sender, "Scene removed: " + name);

                return;
            
            }

        }

        // Handles the 'characters' command
        if ( args.length == 3 ) {

            String command = args[1];
            String sceneName = args[2];
            
            if (command.equals("characters")) {
                
                LOG(LogType.Info, "Scene Name: " + sceneName);

                MessageService.sendPlayerMessage(sender, " - Characters in scene " + sceneName + ": ");
                
                for(String character : ConfigService.getConfig().getConfigurationSection("server.scenes." + sceneName + ".characters").getKeys(false)) {
                    MessageService.sendPlayerMessage(sender, " - " + character);
                }

                return;

            }
            
        
        }

        // Handles the 'add' command
        if ( args.length == 3 ) {

            String command = args[1];
            String sceneId = args[2];
            
            if (command.equals("add")) {
                
                try {
                    
                    // Splitting the Inworld scene id apart to get the scene name
                    String[] sceneParts = sceneId.split("\\/");
                    String sceneName = sceneParts[sceneParts.length-1];

                    // Check if the scene has already been added
                    Object sceneCheck = ConfigService.getConfig().get("server.scenes." + sceneName);
                    if (sceneCheck != null) {
                        MessageService.sendPlayerMessage(sender, "Scene \"" + sceneName  + "\" already exists");
                        return;
                    }

                    // Open a temporary session to retrieve the scene information and confirm
                    // it exists in your Inworld account
                    JSONObject response = APIService.open("0", sceneId, "", "");
                    if (!response.containsKey("character")) {
                        throw new RuntimeException("Error SceneId not found: " + sceneId);
                    }
                    
                    if (!response.containsKey("sessionId")) {
                        throw new RuntimeException("Error unable to add scene");
                    }

                    String sessionId = response.get("sessionId").toString();
                    
                    // Store the scene in the configuration
                    ConfigService.getConfig().set("server.scenes." + sceneName + ".id", sceneId);

                    // Store the scene's characters
                    ArrayList<HashMap<String, String>> sceneChars = (ArrayList<HashMap<String, String>>) response.get("characters");
                    for(int i=0; i < sceneChars.size(); i++) {
                        HashMap<String, String> sceneChar = sceneChars.get(i);
                        String displayName = sceneChar.get("displayName");
                        String nameShort = displayName.replaceAll("\\s", "_");
                        sceneChar.put("nameShort", nameShort);
                        ConfigService.getConfig().set("server.scenes." + sceneName + ".characters." + nameShort, sceneChar);
                    }

                    ConfigService.save();

                    // Output the list of characters to the player
                    MessageService.sendPlayerMessage(sender, "Scene added: " + sceneName);
                    MessageService.sendPlayerMessage(sender, "Characters in scene: ");
                    for(String character : ConfigService.getConfig().getConfigurationSection("server.scenes." + sceneName + ".characters").getKeys(false)) {
                        MessageService.sendPlayerMessage(sender, " - " + character);
                    }

                    APIService.close(sessionId);

                    LOG(LogType.Info, "Scene " + sceneName + " added ");

                } catch ( ConnectException e) {

                    LOG(LogType.Error, "SceneCommand ConnectException: " + e);
                    MessageService.sendPlayerMessage(sender, e.getMessage());
                    return;
        
                } catch ( IOException e) {
        
                    LOG(LogType.Error, "SceneCommand IOException: " + e);
                    MessageService.sendPlayerMessage(sender, "Error unable to add scene");
                    return;
        
                } catch( RuntimeException e) {
                    
                    LOG(LogType.Error, "SceneCommand RuntimeException unable to add scene: " + e.getMessage());
                    MessageService.sendPlayerMessage(sender, e.getMessage());
                    return;

                }

                return;

            }

            if (command.equals("update")) {
                // TODO
                return;
            }

        
        }

        performHelp(sender);
        return;

    }

}
