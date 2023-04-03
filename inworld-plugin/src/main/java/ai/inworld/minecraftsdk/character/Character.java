package ai.inworld.minecraftsdk.character;

import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.utils.ConfigUtils;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import org.bukkit.entity.Villager;
import org.bukkit.Location;


/**
 * This class defines and maps an Inworld character to a Minecraft villager.
 * It's used during character creation as well as when the plugin loads all stored characters.
 */
public class Character {
    
    // The internal Id of the character
    private final String characterId;
    // The short name of the character
    private final String characterName;
    // The long name of the character used in display
    private final String displayName;
    // The Minecraft UID of the Villager entity
    private String uid;
    // The Inworld Id of the character
    private final String id;
    // The spawn location of the Villager in Minecraft
    private Location location;
    // The Inworld scene id of the character
    private final String sceneId;
    // The Inworld scene name for the character
    private final String sceneName;

    /**
     * @param id The Inworld Id
     * @throws RuntimeException
     */
    public Character(String id) throws RuntimeException {
        
        this.id = id;

        // LOG(LogType.Info, "id " + "server.characters." + this.id + ".location" );

        String[] idParts = this.id.split("\\/");
        this.sceneName = idParts[0];
        this.characterName = idParts[1];
        this.sceneId = ConfigService.getConfig().getString("server.scenes." + this.sceneName + ".id");
        this.characterId = ConfigService.getConfig().getString("server.scenes." + this.sceneName + ".characters." + this.characterName + ".resourceName");
        this.displayName = ConfigService.getConfig().getString("server.scenes." + this.sceneName + ".characters." + this.characterName + ".displayName");
        this.uid = ConfigService.getConfig().getString("server.characters." + this.id + ".uid");
        this.location = Location.deserialize(ConfigUtils.castToMap(ConfigService.getConfig().get("server.characters." + this.id + ".location")));

    }

    /**
     * @return Boolean If the character is set to aware or not
     */
    public Boolean getAware() {
        return ConfigService.getConfig().getBoolean("server.characters." + this.id + ".aware");
    }

    /**
     * @return The plugin internal character id
     */
    public String getCharacterId() {
        return this.characterId;
    }
    
    /**
     * @return String The character short name
     */
    public String getCharacterName() {
        return this.characterName;
    }
    
    /**
     * @return String The character long name
     */
    public String getDisplayName() {
        return this.displayName;
    }
    
    /**
     * @return String the Inworld character id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return String the Inworld scene id of the character
     */
    public String getSceneId() {
        return this.sceneId;
    }

    /**
     * @return String the Inworld scene name of the character
     */
    public String getSceneName() {
        return this.sceneName;
    }
    
    /**
     * @return Villager The Minecraft entity in the world
     */
    public Villager getVillager() {
        for (Villager entity: this.location.getWorld().getEntitiesByClass(Villager.class)) {
            if (entity.getUniqueId().toString().equals(this.uid)) {
                return entity;
            }
        }
        throw new RuntimeException("toggleAware Error Entity not found in world");
    }

    /**
     * @return String The Minecraft Villager entity's UID
     */
    public String uid() {
        return this.uid;
    }

    /**
     * Spawns a Villager entity in Minecraft
     * @return Nothing.
     */
    public void spawn() {
        
        try {
            
            // Spawns the entity in the Minecraft world
            Villager entity = this.location.getWorld().spawn(this.location, Villager.class);
            // Sets the entity to not be harmed by enemy mobs
            entity.setInvulnerable(true);
            // Sets the display name of the Villager and displays it
            entity.setCustomName(this.displayName);
            entity.setCustomNameVisible(true);
            // Disables the Villagers awareness so it doesn't move upon spawn
            entity.setAware(false);

            // Gets the UID of the newly created entity and stores it in the config
            this.uid = entity.getUniqueId().toString();
            ConfigService.getConfig().set("server.characters." + this.id + ".uid", this.uid);
            ConfigService.save();
        
        } catch( IllegalArgumentException e ) {
            LOG(LogType.Info, e.getMessage());
        }

    }
    
    /**
     * Toggles the awareness and AI of the Villager and saves it in the config.
     * @return Boolean The awareness state of the villager
     * @throws RuntimeException If the Villager is not found in the World.
     */
    public Boolean toggleAware() throws RuntimeException {
        
        for (Villager entity: this.location.getWorld().getEntitiesByClass(Villager.class)) {
            if (entity.getUniqueId().toString().equals(this.uid)) {
                Boolean aware = !entity.isAware();
                entity.setAI(aware);
                entity.setAware(aware);
                ConfigService.getConfig().set("server.characters." + this.id + ".aware", aware);
                ConfigService.save();
                return entity.isAware();
            }
        }

        throw new RuntimeException("toggleAware Error Entity not found in world");

    }

    /**
     * Removes the Villager entity from the game.
     * @return Nothing.
     */
    public void remove() {

        for (Villager entity: this.location.getWorld().getEntitiesByClass(Villager.class)) {
            if (entity.getUniqueId().toString().equals(this.uid)) {
                // If the Villager is sleeping when this is done wake it up first to avoid
                // the bug with beds always thinking it's still in bed.
                if (entity.isSleeping()) {   
                    entity.wakeup();
                }
                entity.remove();
                
            }
        }
 
    }

}
