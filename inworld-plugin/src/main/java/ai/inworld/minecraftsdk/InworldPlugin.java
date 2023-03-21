package ai.inworld.minecraftsdk;

import org.bukkit.plugin.java.JavaPlugin;

import ai.inworld.minecraftsdk.events.PlayerEvents;
import ai.inworld.minecraftsdk.services.CharacterService;
import ai.inworld.minecraftsdk.services.CommandService;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.ServerService;

import static ai.inworld.minecraftsdk.Constants.CONFIG_FILENAME;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

/*
 * inworld-sdk java plugin
 */
public class InworldPlugin extends JavaPlugin
{

  private CharacterService characterService;
  private CommandService commandService;

  public void onEnable()
  {
    
    ConfigService.load(getDataFolder(), CONFIG_FILENAME);

    this.commandService = new CommandService(this);
    this.characterService = new CharacterService(this);

    getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

    LOG(LogType.Info, "inworld-sdk enabled");
    LOG(LogType.Info, "Server ID: " + ServerService.SERVER_ID);
    LOG(LogType.Info, "IP: " + ServerService.SERVER_IP);

  }

  public void onDisable()
  {
    LOG(LogType.Info, "inworld-sdk disabled");
  }

}
