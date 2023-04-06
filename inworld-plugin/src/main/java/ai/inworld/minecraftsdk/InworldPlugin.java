package ai.inworld.minecraftsdk;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ai.inworld.minecraftsdk.events.PlayerEvents;
import ai.inworld.minecraftsdk.services.CharacterService;
import ai.inworld.minecraftsdk.services.CommandService;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.services.ServerService;
import ai.inworld.minecraftsdk.services.SessionService;

import static ai.inworld.minecraftsdk.Constants.CONFIG_FILENAME;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

/**
 * The InworldPlugin is a Minecraft Java plugin that allows for 
 * 
 * inworld-sdk java plugin
 * 
 * @author Inworld.ai
 * @version 1.0
 * @since 2023-04-03
 */
public class InworldPlugin extends JavaPlugin
{

  private CharacterService characterService;
  private CommandService commandService;
  private ServerService serverService;
  private SessionService sessionService;

  /**
   * Handles initializing the plugin 
   * @return Nothing.
   */
  public void onEnable()
  {
    
    // Loads the plugin configuation data file or creates it
    ConfigService.load(getDataFolder(), CONFIG_FILENAME);

    // Initalizes the services used in the plugin
    this.serverService = new ServerService(this);
    this.commandService = new CommandService(this);
    this.characterService = new CharacterService(this);
    this.sessionService = new SessionService(this);
    
    // Registers the player events with the system
    getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

    LOG(LogType.Info, "inworld-sdk enabled");
    LOG(LogType.Info, "Server ID: " + ServerService.SERVER_ID);
    LOG(LogType.Info, "IP: " + ServerService.SERVER_IP);

  }

  /** 
   * Handles shutting down the plugin/server
   * @return Nothing.
   */ 
  public void onDisable()
  {

    LOG(LogType.Info, "inworld-sdk disabled");
    
    // Stops running the thread that calls the REST service for events
    ServerService.stop();

    // Closes all open Inworld sessions for every player online
    for( Player player: this.getServer().getOnlinePlayers()) {
      SessionService.closeAllSessionByPlayer(player);
    }

  }

}
