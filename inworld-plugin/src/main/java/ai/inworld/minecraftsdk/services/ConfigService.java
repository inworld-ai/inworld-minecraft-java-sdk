package ai.inworld.minecraftsdk.services;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static ai.inworld.minecraftsdk.utils.Log.logConsole;
import static ai.inworld.minecraftsdk.utils.Log.LogType.Error;
import static ai.inworld.minecraftsdk.utils.Log.LogType.Info;

public class ConfigService {
    
    private static FileConfiguration config;
    private static File configFile;
    private static File configFolderPath;

    public static void load(File dataFolder, String filename) {

        logConsole(Info, "Loading config file \"" + dataFolder.getPath() + "/" + filename + "\"");

        configFolderPath = dataFolder;
        configFile = new File(configFolderPath, filename);

        if (!configFile.exists()){
            try {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
                process();
                config.set("server.api.dev", "http://localhost:3000");
                config.set("server.api.prod", "");
                save();
            } catch (IOException e) {
                logConsole(Error, "Could not create file \"" + dataFolder.getPath() + "/" + filename + "\"");
            }
        } else {
            process();
        }

    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reset() {
        process();
    }

    private static void process() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

}
