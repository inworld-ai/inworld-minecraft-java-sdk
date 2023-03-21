package ai.inworld.minecraftsdk.utils;

import java.util.Map;

import org.bukkit.configuration.MemorySection;

public class ConfigUtils {

    /**
     * If loading from file the Maps in the data
     * will be stored as a MemorySection not Maps.
     * 
     * @param entry    MemorySection or Map to check.
     * @return        Map containing the serialised data.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> castToMap(Object entry) {

        if (entry instanceof MemorySection) {
            return ((MemorySection) entry).getValues(true);
        } else {
            return (Map<String, Object>) entry;
        }
    }

}
