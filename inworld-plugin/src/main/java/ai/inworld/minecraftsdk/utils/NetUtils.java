package ai.inworld.minecraftsdk.utils;

import org.bukkit.Bukkit;

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
        if (ip.equals("")) {
            ip = "localhost";
        }
        return ip;
    }
    
}
