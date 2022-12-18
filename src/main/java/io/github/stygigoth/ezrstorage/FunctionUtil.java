package io.github.stygigoth.ezrstorage;

import java.util.function.Function;

public final class FunctionUtil {
    private FunctionUtil() {
        throw new AssertionError();
    }

    @SuppressWarnings("unchecked")
    public static <T, R> Function<T, R> uncheckedCast() {
        return t -> (R)t;
    }
}
