package ai.inworld.minecraftsdk.utils;

import java.util.StringJoiner;

/**
 * This class contains helper functions for Strings
 */
public class StringUtils {

   /**
    * This method joining an array Strings to a single String.
    * @param stringArray The array of strings
    * @return String 
    */
    public static String arrayToString(String stringArray[]) {
      StringJoiner joiner = new StringJoiner(" ");
      for (int i = 0; i < stringArray.length; i++) {
         joiner.add(stringArray[i]);
      }
      String sentence = joiner.toString();
      return sentence;
   }

}
