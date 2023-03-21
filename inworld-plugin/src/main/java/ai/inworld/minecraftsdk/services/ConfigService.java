package ai.inworld.minecraftsdk.services;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType.Error;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType.Info;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigService {
    
    private static FileConfiguration config;
    private static File configFile;
    private static File configFolderPath;

    public static void load(File dataFolder, String filename) {

        LOG(Info, "Loading config file \"" + dataFolder.getPath() + "/" + filename + "\"");

        configFolderPath = dataFolder;
        configFile = new File(configFolderPath, filename);

        if (!configFile.exists()){
            try {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
                init();
            } catch (IOException e) {
                LOG(Error, "Could not create file \"" + dataFolder.getPath() + "/" + filename + "\"");
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

    private static void init() {
        process();
        config.set("server.api.dev", "http://localhost:3000");
        config.set("server.api.prod", "");
        config.set("server.scenes", new HashMap<>());
        config.set("server.characters", new HashMap<>());
        save();
    }

    private static void process() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

}
