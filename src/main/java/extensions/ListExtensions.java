package extensions;

import java.util.Collections;
import java.util.List;

public class ListExtensions {
    public static <T extends Comparable<? super T>> void reverseList(List<T> list) {
        Collections.sort(list);
        Collections.reverse(list);
    }
}
