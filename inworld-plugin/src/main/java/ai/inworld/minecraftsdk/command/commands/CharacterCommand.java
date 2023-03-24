package ai.inworld.minecraftsdk.command.commands;

import ai.inworld.minecraftsdk.character.Character;
import ai.inworld.minecraftsdk.command.Command;
import ai.inworld.minecraftsdk.command.CommandBase;
import ai.inworld.minecraftsdk.services.CharacterService;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.MessageService;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import java.util.ArrayList;
import java.util.List;

public class CharacterCommand extends CommandBase implements Command {
    
    public CharacterCommand() {
        
        this.permission = "inworld.command.character";
        this.description = "Adds, Removes, Lists Character NPCs";
        this.name = "character";
        this.version = "1.0";
        this.syntax = "/inworld character [command] [scene] [character]";
        this.minArgs = 2;
        this.maxArgs = 4;

        final List<String> commandList = new ArrayList<>();
        commandList.add("add");
        commandList.add("remove");
        commandList.add("list");
        commandList.add("toggleAware");
        tabCompletes.put(2, commandList);

    }

    @Override
    public List<String> getTabComplete(String[] args, int index) {

        if ( args.length >= 2 ) {
        
            String command = args[1];

            if (command.equals("add")) {
                final List<String> sceneList = new ArrayList<>();
                for(String scene : ConfigService.getConfig().getConfigurationSection("server.scenes").getKeys(false)) {
                    sceneList.add(scene);
                }
                tabCompletes.put(3, sceneList);
            }
            
        }

        if ( args.length == 4 ) {
        
            String command = args[1];
            String sceneId = args[2];

            if (command.equals("add") && !sceneId.equals("")) {
                final List<String> characterList = new ArrayList<>();
                for(String character : ConfigService.getConfig().getConfigurationSection("server.scenes." + sceneId + ".characters").getKeys(false)) {
                    characterList.add(character);
                }
                tabCompletes.put(4, characterList);
            }
            
        }

        if ( args.length == 2 || args.length == 3 ) {
        
            String command = args[1];

            if (command.equals("remove") || command.equals("toggleAware")) {
                final List<String> characterList = new ArrayList<>();
                for(String character : ConfigService.getConfig().getConfigurationSection("server.characters").getKeys(false)) {
                    characterList.add(character);
                }
                tabCompletes.put(3, characterList);
            }
            
        }

        return super.getTabComplete(args, index);

    }

    @Override
    protected void processCommand(Player sender, String[] args) {
        
        if ( args.length == 2 ) {

            String command = args[1];
            
            if (command.equals("list")) {

                MessageService.sendPlayerMessage(sender, "Characters in game:");
                for(String character : ConfigService.getConfig().getConfigurationSection("server.characters").getKeys(false)) {
                    MessageService.sendPlayerMessage(sender, "- " + character);
                }

                return;

            }

        }

        if ( args.length == 3 ) {
            
            String command = args[1];
            
            if (command.equals("remove")) {
                
                String id = args[2];

                Object characterCheck = ConfigService.getConfig().get("server.characters." + id);
                if (characterCheck == null) {
                    MessageService.sendPlayerMessage(sender, "Character " + id + " doesn't exist in game");
                    return;
                }

                CharacterService.removeEntity(id);
                MessageService.sendPlayerMessage(sender, "Character removed: " + id);

                return;
            
            }

        }

        if ( args.length >= 3 ) {

            String command = args[1];
            
            if (command.equals("add")) {
                
                String sceneId = args[2];
                
                Object sceneCheck = ConfigService.getConfig().get("server.scenes." + sceneId);
                if (sceneCheck == null) {
                    MessageService.sendPlayerMessage(sender, "Scene \"" + sceneId + "\" doesn't exist");
                    return;
                }

                String characterId = args[3];
                String id = sceneId + "/" + characterId;

                Object characterCheck = ConfigService.getConfig().get("server.scenes." + sceneId + ".characters." + characterId);
                if (characterCheck == null) {
                    MessageService.sendPlayerMessage(sender, "Character \"" + characterId + "\" doesn't exist in scene \"" + sceneId + "\"");
                    return;
                }
                
                characterCheck = ConfigService.getConfig().get("server.characters." + id);
                if (characterCheck != null) {
                    MessageService.sendPlayerMessage(sender, "Character " + id + " already exist in game");
                    return;
                }

                CharacterService.addEntity(id, sender.getLocation());
                MessageService.sendPlayerMessage(sender, "Character added: " + id);

                return;

            }
        
        }

        if ( args.length == 3 ) {
            
            String command = args[1];
            
            if (command.equals("toggleAware")) {
                
                String id = args[2];

                Object characterCheck = ConfigService.getConfig().get("server.characters." + id);
                if (characterCheck == null) {
                    MessageService.sendPlayerMessage(sender, "Character " + id + " doesn't exist in game");
                    return;
                }

                try {
                    Boolean awareState = CharacterService.toggleAware(id);
                    MessageService.sendPlayerMessage(sender, "Character awareness is: " + awareState);
                } catch (RuntimeException e) {
                    LOG(LogType.Error, e.getMessage());
                    MessageService.sendPlayerMessage(sender, "Error toggling awareness: " + e.getMessage());
                }

                return;
            
            }

        }

        performHelp(sender);
        return;

    }

}
