package ai.inworld.minecraftsdk.services;

import ai.inworld.minecraftsdk.character.Character;
import ai.inworld.minecraftsdk.events.EntityEvents;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CharacterService {
    
    private static final HashMap<String, Character> characters = new HashMap<>();
    private final JavaPlugin plugin;

    public CharacterService(JavaPlugin plugin) {

        this.plugin = plugin;

        this.plugin.getServer().getPluginManager().registerEvents(new EntityEvents(), this.plugin);

        for(String characterId : ConfigService.getConfig().getConfigurationSection("server.characters").getKeys(false)) {
            characters.put(characterId, new Character(characterId));
        }

    }

    public static void addEntity(String characterId, Location location) {

        ConfigService.getConfig().set("server.characters." + characterId + ".location", location.serialize());
        ConfigService.getConfig().set("server.characters." + characterId + ".uid", "0");
        ConfigService.getConfig().set("server.characters." + characterId + ".aware", false);
        ConfigService.save();

        Character character = new Character(characterId);
        if ( character != null ) {
            characters.put(characterId, character);
            character.spawn();
        }

    }
    
    public static Character getCharacterById(String characterId) {
        return characters.get(characterId);
    }

    public static String getIdByUid(String uid) throws RuntimeException {

        // LOG(LogType.Info, "getIdByUid:" + uid);

        for(String characterId : ConfigService.getConfig().getConfigurationSection("server.characters").getKeys(false)) {
            if(uid.equals(ConfigService.getConfig().getString("server.characters." + characterId + ".uid"))) {
                return characterId;
            }
        }

        throw new RuntimeException("Character UID not found");

    }

    public static void removeEntity(String characterId) {
        
        // TODO Close all active sessions

        Character character = characters.get(characterId);
        if ( character != null ) {
            character.remove();
            characters.remove(characterId);
        }
   
        ConfigService.getConfig().set("server.characters." + characterId, null);
        ConfigService.save();
        
    } 

    public static Boolean toggleAware(String id) throws RuntimeException {
        
        try {

            Character character = characters.get(id);
            if ( character != null ) {
                return character.toggleAware();
            }

            throw new RuntimeException("Error: CharacterService toggleAware character " + id + " not found in service");

        } catch ( RuntimeException e) {
            throw e;
        }
        
    }

}
