package ai.inworld.minecraftsdk.utils;

import org.bukkit.Bukkit;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;

/**
 * This class is a helper for network commands
 */
public class NetUtils {

    /**
     * This method determines if the server is running on localhost
     * @return String IP address of server or localhost
     */
    public static String getIp() {
        String ip = Bukkit.getIp();
        LOG(LogType.Info, "ServerIP Is: " + ip);
        if (ip.equals("")) {
            ip = "localhost";
        }
        return ip;
    }
    
}
