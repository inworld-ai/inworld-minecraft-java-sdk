package ai.inworld.minecraftsdk.command.commands;

import org.json.simple.JSONObject;

import ai.inworld.minecraftsdk.command.Command;
import ai.inworld.minecraftsdk.command.CommandBase;
import ai.inworld.minecraftsdk.services.APIService;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.MessageService;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// workspaces/inworld_wonderland_roblox/scenes/the_caterpillars_mushroom
        
// ConfigService.getConfig().set("server.scenes." + sceneName + ".characters", sceneChars); //response.get("characters")

// ArrayList<HashMap<String, HashMap<String, String>>> sec = (ArrayList<HashMap<String, HashMap<String, String>>>) ConfigService.getConfig().get("server.scenes." + name + ".characters");
// MessageService.sendPlayerMessage(sender, " - Characters in scene " + name + ": ");

// for(int i=0; i < sec.size(); i++) {
//     HashMap<String, HashMap<String, String>> character = (HashMap<String, HashMap<String, String>>) sec.get(i);
//     MessageService.sendPlayerMessage(sender, "ID: " + Integer.toString(i+1) + " Name: " + character.get("displayName"));
// }

// ArrayList<HashMap<String, String>> sec = (ArrayList<HashMap<String, String>>) ConfigService.getConfig().getList("server.scenes." + name + ".characters");

// for(int i=0; i < sec.size(); i++) {
//     HashMap<String, String> character = (HashMap<String, String>) sec.get(i);
//     MessageService.sendPlayerMessage(sender, "ID: " + Integer.toString(i+1) + " Name: " + character.get("displayName"));
// }
            
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
        commandList.add("update");
        tabCompletes.put(2, commandList);

    }

    @Override
    public List<String> getTabComplete(String[] args, int index) {

        if ( args.length == 2 ) {
        
            String command = args[1];
            final List<String> sceneList = new ArrayList<>();

            if (command.equals("remove")
            || command.equals("characters")
            || command.equals("update")) {
                for(String scene : ConfigService.getConfig().getConfigurationSection("server.scenes").getKeys(false)) {
                    sceneList.add(scene);
                }
            }
            
            tabCompletes.put(3, sceneList);

        }

        return super.getTabComplete(args, index);

    }

    @Override
    protected void processCommand(Player sender, String[] args) {
        
        if ( args.length == 2 ) {

            String command = args[1];
            
            if (command.equals("list")) {

                MessageService.sendPlayerMessage(sender, "Scenes in system:");
                for(String scene : ConfigService.getConfig().getConfigurationSection("server.scenes").getKeys(false)) {
                    MessageService.sendPlayerMessage(sender, "- " + scene);
                }

                return;

            }

        }

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

        if ( args.length == 3 ) {

            String command = args[1];
            String sceneId = args[2];
            
            if (command.equals("add")) {
                
                try {

                    String[] sceneParts = sceneId.split("\\/");
                    String sceneName = sceneParts[sceneParts.length-1];

                    Object sceneCheck = ConfigService.getConfig().get("server.scenes." + sceneName);

                    if (sceneCheck != null) {
                        MessageService.sendPlayerMessage(sender, "Scene \"" + sceneName  + "\" already exists");
                        return;
                    }

                    JSONObject response = APIService.open("0", sceneId, "", "");

                    if (!response.containsKey("character")) {
                        throw new RuntimeException("Error SceneId not found: " + sceneId);
                    }
                    
                    if (!response.containsKey("sessionId")) {
                        throw new RuntimeException("Error unable to add scene");
                    }

                    String sessionId = response.get("sessionId").toString();
                    
                    ConfigService.getConfig().set("server.scenes." + sceneName + ".id", sceneId);

                    ArrayList<HashMap<String, String>> sceneChars = (ArrayList<HashMap<String, String>>) response.get("characters");

                    for(int i=0; i < sceneChars.size(); i++) {
                        HashMap<String, String> sceneChar = sceneChars.get(i);
                        String displayName = sceneChar.get("displayName");
                        String nameShort = displayName.replaceAll("\\s", "_");
                        sceneChar.put("nameShort", nameShort);
                        ConfigService.getConfig().set("server.scenes." + sceneName + ".characters." + nameShort, sceneChar);
                    }

                    ConfigService.save();

                    MessageService.sendPlayerMessage(sender, "Scene added: " + sceneName);

                    MessageService.sendPlayerMessage(sender, " - Characters in scene " + sceneName + ": ");

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
