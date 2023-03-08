package ai.inworld.minecraftsdk;

import org.bukkit.plugin.java.JavaPlugin;

import ai.inworld.minecraftsdk.events.PlayerEvents;
import ai.inworld.minecraftsdk.services.CommandService;
import ai.inworld.minecraftsdk.services.ConfigService;
import ai.inworld.minecraftsdk.utils.NetUtils;


import static ai.inworld.minecraftsdk.Constants.CONFIG_FILENAME;
import static ai.inworld.minecraftsdk.utils.Log.logConsole;
import static ai.inworld.minecraftsdk.utils.Log.LogType;

/*
 * inworld-sdk java plugin
 */
public class InworldPlugin extends JavaPlugin
{

  private CommandService commandService;

  public void onEnable()
  {
    
    ConfigService.load(getDataFolder(), CONFIG_FILENAME);

    this.commandService = new CommandService("inworld.command");
    getCommand("inworld").setExecutor(this.commandService);
    getCommand("inworld").setTabCompleter(this.commandService);

    getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

    logConsole(LogType.Info, "inworld-sdk enabled");

  }

  public void onDisable()
  {
    logConsole(LogType.Info, "inworld-sdk disabled");
  }

}
