package ai.inworld.minecraftsdk;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * minecraft-sdk java plugin
 */
public class Plugin extends JavaPlugin
{
  private static final Logger LOGGER=Logger.getLogger("minecraft-sdk");

  public void onEnable()
  {
    LOGGER.info("minecraft-sdk enabled");
  }

  public void onDisable()
  {
    LOGGER.info("minecraft-sdk disabled");
  }
}
