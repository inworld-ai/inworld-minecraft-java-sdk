package ai.inworld.minecraftsdk;

import org.bukkit.plugin.java.JavaPlugin;

import ai.inworld.minecraftsdk.events.PlayerEvents;
import ai.inworld.minecraftsdk.utils.NetUtils;
import static ai.inworld.minecraftsdk.utils.Log.logConsole;
import static ai.inworld.minecraftsdk.utils.Log.LogType;

/*
 * inworld-sdk java plugin
 */
public class InworldPlugin extends JavaPlugin
{
  // private static final Logger LOGGER=Logger.getLogger("inworld-sdk");

  public void onEnable()
  {
    getServer().getPluginManager().registerEvents(new PlayerEvents(),this);
    logConsole(LogType.Info, "inworld-sdk enabled");
  }

  public void onDisable()
  {
    logConsole(LogType.Info, "inworld-sdk disabled");
  }
}
