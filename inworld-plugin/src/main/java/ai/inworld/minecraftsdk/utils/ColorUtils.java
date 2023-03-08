package ai.inworld.minecraftsdk.utils;

import java.util.List;

public class ColorUtils {

    public static String translate(String message) {
        return message.replace("&", "§");
    }

    public static List<String> translate(List<String> lore) {
        lore.replaceAll(ColorUtils::translate);
        return lore;
    }

}