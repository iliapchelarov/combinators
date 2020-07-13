package com.scalefocus.camp.combinators;

import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface Before<T, R> extends Function<Consumer<T>, Function<Function<T, R>, Function<T, R>>> {

    static <T, R> Before<T, R> create() {
        return before -> function -> t -> {
            before.accept(t);
            return function.apply(t);
        };
    }

    static <T, R> Function<T, R> decorate(Consumer<T> before, Function<T, R> function) {
        return Before.<T, R>create().apply(before).apply(function);
    }

    static <T, R> Decorator<T, R> before(Consumer<T> before) {
        return f -> decorate(before, f);
    }

    static <T, R> R applyBefore(Consumer<T> before, Function<T, R> function, T to) {
        return Before.decorate(before, function).apply(to);
    }

}
