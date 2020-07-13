package com.scalefocus.camp.combinators;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public interface Postcondition<T, R, X extends RuntimeException>
        extends Function<Function<T, R>,
        Function<BiPredicate<T, R>,
                Function<BiFunction<T, R, X>,
                        Function<T, R>>>> {

    static <T, R, X extends RuntimeException> Postcondition<T, R, X> create() {
        return function -> condition -> error ->
                After.decorate(function,
                        (t, r) -> {
                            if (!condition.test(t, r)) {
                                throw error.apply(t, r);
                            }
                        });
    }

    static <T, R, X extends RuntimeException> Function<T, R> decorate(
            Function<T, R> function,
            BiPredicate<T, R> condition,
            BiFunction<T, R, X> error) {
        return Postcondition.<T, R, X>create().apply(function).apply(condition).apply(error);
    }

    static <T, R> Decorator<T, R> postcondition(BiPredicate<T, R> condition) {
        return f -> decorate(f, condition, Postcondition::illegalState);
    }

    static <T, R> RuntimeException illegalState(T t, R r) {
        return new IllegalStateException("Postcondition failed for argument: " + String.valueOf(t) + " and result: " + String.valueOf(r));
    }

}
