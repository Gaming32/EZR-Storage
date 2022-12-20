package io.github.gaming32.ezrstorage.util;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class MoreCollectors {
    public static <T extends Collection<E>, E> Collector<E, T, T> customCollection(
        Supplier<T> listCreator,
        Collector.Characteristics... characteristics
    ) {
        return Collector.of(
            listCreator, Collection::add,
            (list1, list2) -> {
                final T result = listCreator.get();
                result.addAll(list1);
                result.addAll(list2);
                return result;
            }, characteristics
        );
    }
}
