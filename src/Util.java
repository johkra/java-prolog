import java.util.ArrayList;
import java.util.List;

/**
 * User: Johannes Krampf <johkra@gmail.com>
 * Date: 12.02.11
 */
public class Util {
    public static List<String> split(String l, char sep, Boolean ALL) {
        int nest = 0;
        ArrayList<String> returnValue = new ArrayList<String>();
        if (l.equals("")) {
            return returnValue;
        }
        for (int i = 0; i < l.length(); i++) {
            char c = l.charAt(i);
            if ((nest <= 0) && (c == sep)) {
                if (ALL) {
                    returnValue.add(l.substring(0, i));
                    returnValue.addAll(split(l.substring(i + 1), sep, ALL));
                } else {
                    returnValue.add(l.substring(0, i));
                    returnValue.add(l.substring(i + 1));

                }
                return returnValue;
            }
            if ((c == '[') || (c == '(')) {
                nest += 1;
            }
            if ((c == ']') || (c == ')')) {
                nest -= 1;
            }
        }
        returnValue.add(l);
        return returnValue;
    }
}
