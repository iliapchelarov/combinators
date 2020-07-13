package com.scalefocus.camp.combinators;

import java.util.function.BiConsumer;
import java.util.function.Function;

@FunctionalInterface
public interface After<T, R> extends Function<Function<T, R>,
        Function<BiConsumer<T, R>,
                Function<T, R>>> {

    static <T, R> After<T, R> create() {
        return function -> after -> t -> {
            R r = function.apply(t);
            after.accept(t, r);
            return r;
        };
    }

    static <T, R> Function<T, R> decorate(Function<T, R> function, BiConsumer<T, R> after) {
        return After.<T, R>create().apply(function).apply(after);
    }

    static <T, R> Decorator<T, R> after(BiConsumer<T, R> after) {
        return f -> decorate(f, after);
    }

    static <T, R> R applyAfter(Function<T, R> function, BiConsumer<T, R> after, T to) {
        return After.decorate(function, after).apply(to);
    }
}
