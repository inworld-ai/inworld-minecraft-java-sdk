package ai.inworld.minecraftsdk.services;

import ai.inworld.minecraftsdk.character.Character;
import ai.inworld.minecraftsdk.events.EntityEvents;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This service class manages the collection of all Inworld Characters as Minecraft Villagers 
 * on the server. It primarily is called by the 'character' command from the Player.
 */
public class CharacterService {
    
    private static final HashMap<String, Character> characters = new HashMap<>();
    private final JavaPlugin plugin;

    /**
     * This method is called upon enabling the plugin. It loads all the characters stored in the
     * configuration file into the collection.
     * @param plugin A reference to the Minecraft plugin
     */
    public CharacterService(JavaPlugin plugin) {

        this.plugin = plugin;

        this.plugin.getServer().getPluginManager().registerEvents(new EntityEvents(), this.plugin);

        if (ConfigService.getConfig().getConfigurationSection("server.characters") == null) {
            return;
        }

        for(String characterId : ConfigService.getConfig().getConfigurationSection("server.characters").getKeys(false)) {
            characters.put(characterId, new Character(characterId));
        }

    }

    /**
     * This method handles adding a new character to the server
     * @param characterId The internal character id 
     * @param location The spawn location of the character
     */
    public static void addEntity(String characterId, Location location) {

        // Add the data to the configuration
        ConfigService.getConfig().set("server.characters." + characterId + ".location", location.serialize());
        ConfigService.getConfig().set("server.characters." + characterId + ".uid", "0");
        ConfigService.getConfig().set("server.characters." + characterId + ".aware", false);
        ConfigService.save();

        // Create the new character and spawn it in the world
        Character character = new Character(characterId);
        if ( character != null ) {
            characters.put(characterId, character);
            character.spawn();
        }

    }
    
    /**
     * Returns the character from the collection by it's internal character id
     * @param characterId The internal character id
     * @return Character The character associated with the character id
     */
    public static Character getCharacterById(String characterId) {
        return characters.get(characterId);
    }

    /**
     * This method returns the internal character id of a villager by a Minecraft
     * Villager entity UID
     * @param uid The Minecraft Villager entity UID as String
     * @return String the internal character id
     * @throws RuntimeException Thrown if the Villager entity UID is not found
     */
    public static String getIdByUid(String uid) throws RuntimeException {

        // LOG(LogType.Info, "getIdByUid:" + uid);

        for(String characterId : ConfigService.getConfig().getConfigurationSection("server.characters").getKeys(false)) {
            if(uid.equals(ConfigService.getConfig().getString("server.characters." + characterId + ".uid"))) {
                return characterId;
            }
        }

        throw new RuntimeException("Character UID not found");

    }

    /**
     * This method removes the character from the server
     * @param characterId The internal character id
     */
    public static void removeEntity(String characterId) {
        
        // TODO Close all active sessions
        // TODO Error handling

        Character character = characters.get(characterId);
        if ( character != null ) {
            character.remove();
            characters.remove(characterId);
        }
   
        ConfigService.getConfig().set("server.characters." + characterId, null);
        ConfigService.save();
        
    } 

    /**
     * This method toggles the aware and ai state of the character
     * @param characterId The internal character id
     * @return Boolean The aware state of the character
     * @throws RuntimeException Thrown if the character id is not found
     */
    public static Boolean toggleAware(String characterId) throws RuntimeException {
        
        try {

            Character character = characters.get(characterId);
            if ( character != null ) {
                return character.toggleAware();
            }

            throw new RuntimeException("Error: CharacterService toggleAware character " + characterId + " not found in service");

        } catch ( RuntimeException e) {
            throw e;
        }
        
    }

}
