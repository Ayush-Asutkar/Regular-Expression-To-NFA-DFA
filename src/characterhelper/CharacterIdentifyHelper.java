package characterhelper;

public class CharacterIdentifyHelper {
    public static boolean isLatinLetter (char ch) {
        if ((ch >= 'a')  &&  (ch <= 'z')) {
            //small case letter
            return true;
        }

        if ((ch >= 'A')  &&  (ch <= 'Z')) {
            //upper case letter
            return true;
        }

        return false;
    }
}
