package extensions;

import java.util.Collections;
import java.util.List;

public class ListExtensions {
    public static void reverseList(List list) {
        Collections.sort(list);
        Collections.reverse(list);
    }
}
