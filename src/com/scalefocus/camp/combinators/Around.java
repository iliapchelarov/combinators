package com.scalefocus.camp.combinators;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Function;

@FunctionalInterface
public interface Around<T, R> extends Function<Function<T, R>,
        Function<BiConsumer<Callable<R>, T>,
                Function<T, R>>> {

    static <T, R> Around<T, R> create() {
        return function -> around -> t -> {
            // Lambdas do not allow out-of-scope objects to be changed, but do allow arrays to be assigned...
            R[] result = (R[]) new Object[1];
            around.accept(
                    () -> result[0] = function.apply(t),
                    t
            );
            return result[0];
        };
    }

    static <T, R> Function<T, R> decorate(Function<T, R> function, BiConsumer<Callable<R>, T> around) {
        return Around.<T, R>create().apply(function).apply(around);
    }

    static <T, R> Decorator<T, R> around(BiConsumer<Callable<R>, T> around) {
        return f -> decorate(f, around);
    }

    static <T, R> R applyAround(Function<T, R> function, BiConsumer<Callable<R>, T> around, T to) {
        return Around.decorate(function, around).apply(to);
    }
}
