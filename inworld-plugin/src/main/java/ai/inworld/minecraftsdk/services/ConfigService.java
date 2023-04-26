package ai.inworld.minecraftsdk.services;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType.Error;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType.Info;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * This service class manages the creation, loading and saving of the plugin's configuration 
 */
public class ConfigService {
    
    private static FileConfiguration config;
    private static File configFile;
    private static File configFolderPath;

    /**
     * This method loads the configuration file 
     * @param dataFolder The File object representing the directory path of the configuration file
     * @param filename The filename of the configuration file
     */
    public static void load(File dataFolder, String filename) {

        LOG(Info, "Loading config file \"" + dataFolder.getPath() + "/" + filename + "\"");

        configFolderPath = dataFolder;
        configFile = new File(configFolderPath, filename);

        // Check if the file exists already
        if (!configFile.exists()){
            try {
                // Create the directories
                configFile.getParentFile().mkdirs();
                // Create the file
                configFile.createNewFile();
                // Initialize the configuration file
                init();
            } catch (IOException e) {
                LOG(Error, "Could not create file \"" + dataFolder.getPath() + "/" + filename + "\"");
            }
        } else {
            // If found process the configuration file
            process();
        }

    }

    /**
     * This method returns the configuration file
     * @return FileConfiguration The configuration file
     */
    public static FileConfiguration getConfig() {
        return config;
    }

    /**
     * This method saves the configuration file
     * @return Nothing.
     */
    public static void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method resets the configuration file
     * @return Nothing.
     */
    public static void reset() {
        process();
    }

    /**
     * This method initalizes the configuration file with the defaults
     * @return Nothing.
     */
    private static void init() {
        process();
        config.set("server.api.key", "");
        config.set("server.api.secret", "");
        config.set("server.scenes", new HashMap<>());
        config.set("server.characters", new HashMap<>());
        save();
    }

    /**
     * This method processing the configuration file into the plugin
     */
    public static void process() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

}
