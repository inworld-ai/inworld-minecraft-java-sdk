package ai.inworld.minecraftsdk.services;

import ai.inworld.minecraftsdk.character.Character;
import ai.inworld.minecraftsdk.events.EntityEvents;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static void addEntity(String id) {

        Character character = new Character(id);
        if ( character != null ) {
            characters.put(id, character);
            character.spawn();
        }

    }
    
    public static void removeEntity(String id) {
        
        Character character = characters.get(id);
        if ( character != null ) {
            character.remove();
            characters.remove(id);
        }
   
    } 

}
