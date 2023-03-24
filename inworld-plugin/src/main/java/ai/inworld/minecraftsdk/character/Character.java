package ai.inworld.minecraftsdk.character;

import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.utils.ConfigUtils;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

import org.bukkit.entity.Villager;
import org.bukkit.Location;

public class Character {
    
    private final String characterId;
    private final String characterName;
    private final String displayName;
    private String uid;
    private final String id;
    private Location location;
    private final String sceneId;
    private final String sceneName;

    public Character(String id) throws RuntimeException {
        
        this.id = id;

        LOG(LogType.Info, "id " + "server.characters." + this.id + ".location" );

        String[] idParts = this.id.split("\\/");
        this.sceneName = idParts[0];
        this.characterName = idParts[1];
        this.sceneId = ConfigService.getConfig().getString("server.scenes." + this.sceneName + ".id");
        this.characterId = ConfigService.getConfig().getString("server.scenes." + this.sceneName + ".characters." + this.characterName + ".resourceName");
        this.displayName = ConfigService.getConfig().getString("server.scenes." + this.sceneName + ".characters." + this.characterName + ".displayName");
        this.uid = ConfigService.getConfig().getString("server.characters." + this.id + ".uid");
        this.location = Location.deserialize(ConfigUtils.castToMap(ConfigService.getConfig().get("server.characters." + this.id + ".location")));

    }

    public Boolean getAware() {
        return ConfigService.getConfig().getBoolean("server.characters." + this.id + ".aware");
    }

    public String getCharacterId() {
        return this.characterId;
    }
    
    public String getCharacterName() {
        return this.characterName;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public String getId() {
        return this.id;
    }

    public String getSceneId() {
        return this.sceneId;
    }

    public String getSceneName() {
        return this.sceneName;
    }
    
    public Villager getVillager() {
        for (Villager entity: this.location.getWorld().getEntitiesByClass(Villager.class)) {
            if (entity.getUniqueId().toString().equals(this.uid)) {
                return entity;
            }
        }
        throw new RuntimeException("toggleAware Error Entity not found in world");
    }

    public String uid() {
        return this.uid;
    }

    public void spawn() {
        
        try {
        
            Villager entity = this.location.getWorld().spawn(this.location, Villager.class);
            entity.setInvulnerable(true);
            entity.setCustomName(this.displayName);
            entity.setCustomNameVisible(true);
            entity.setAware(false);

            this.uid = entity.getUniqueId().toString();
            ConfigService.getConfig().set("server.characters." + this.id + ".uid", this.uid);
            ConfigService.save();
        
        } catch( IllegalArgumentException e ) {
            LOG(LogType.Info, e.getMessage());
        }

    }
    
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

    public void remove() {

        for (Villager entity: this.location.getWorld().getEntitiesByClass(Villager.class)) {
            if (entity.getUniqueId().toString().equals(this.uid)) {

                if (entity.isSleeping()) {   
                    entity.wakeup();
                }
                entity.remove();
                
            }
        }
 
    }

}
