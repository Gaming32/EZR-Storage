package io.github.stygigoth.ezrstorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListUtil {
    public static <E> boolean addSorted(List<E> list, E element, Comparator<E> comparator) {
        int index = Collections.binarySearch(list, element, comparator);
        final boolean isNew = index < 0;
        if (isNew) {
            index = -index - 1;
        }
        list.add(index, element);
        return isNew;
    }
}
