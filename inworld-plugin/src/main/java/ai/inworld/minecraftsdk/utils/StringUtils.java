package ai.inworld.minecraftsdk.utils;

import java.util.StringJoiner;

public class StringUtils {

    public static String arrayToString(String stringArray[]) {
        StringJoiner joiner = new StringJoiner(" ");
        for(int i = 0; i < stringArray.length; i++) {
           joiner.add(stringArray[i]);
        }
        String sentence = joiner.toString();
        return sentence;
     }

}
