package ai.inworld.minecraftsdk.utils;

import org.bukkit.Bukkit;

public class NetUtils {
    // Get the server IP
    public static String getIp() {
        String ip = Bukkit.getIp();
        if (ip.equals("")) {
            ip = "localhost";
        }
        return ip;
    }
}
