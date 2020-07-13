package com.scalefocus.camp.combinators;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Precondition<T, R, X extends RuntimeException>
        extends Function<Predicate<T>,
        Function<Function<T, R>,
                Function<Function<T, X>,
                        Function<T, R>>>> {

    static <T, R, X extends RuntimeException> Precondition<T, R, X> create() {
        return condition -> function -> error ->
                Provided.decorate(
                        condition,
                        function,
                        t -> {
                            throw error.apply(t);
                        });
    }

    static <T, R, X extends RuntimeException> Function<T, R> decorate(
            Predicate<T> condition,
            Function<T, R> function,
            Function<T, X> error) {
        return Precondition.<T, R, X>create().apply(condition).apply(function).apply(error);
    }

    static <T, R> Decorator<T, R> precondition(Predicate<T> condition,
                                               Function<T, RuntimeException> error) {
        return f -> Precondition.<T, R, RuntimeException>decorate(
                condition, f, error
        );
    }

    static <T, R> Decorator<T, R> precondition(Predicate<T> condition) {
        return Precondition.precondition(
                condition, Precondition::illegalArgument
        );
    }

    static <T, R> Decorator<T, R> notNull() {
        return Precondition.precondition(t -> t != null, Precondition::nullPointer);
    }

    static <T> RuntimeException illegalArgument(T t) {
        return new IllegalArgumentException("Precondition failed for argument: " + t);
    }

    static <T> RuntimeException nullPointer(T t) {
        return new NullPointerException("Argument is " + t);
    }
}
