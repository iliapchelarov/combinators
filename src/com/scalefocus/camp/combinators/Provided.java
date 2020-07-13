package com.scalefocus.camp.combinators;

import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Provided<T, R> extends Function<Predicate<T>,
        Function<Function<T, R>,
                Function<Function<T, R>,
                        Function<T, R>>>> {

    static <T, R> Provided<T, R> create() {
        return condition -> function -> fallback -> t ->
                (condition.test(t) ? function : fallback).apply(t)
                ;
    }

    static <T, R> Function<T, R> decorate(Predicate<T> condition,
                                          Function<T, R> function,
                                          Function<T, R> fallback) {
        return Provided.<T, R>create().apply(condition).apply(function).apply(fallback);
    }

    static <T, R> Decorator<T, R> provided(Predicate<T> condition, Function<T, R> fallback) {
        return f -> Provided.decorate(condition, f, fallback);
    }

    static <T, R> Decorator<T, R> provided(Predicate<T> condition) {
        return f -> Provided.decorate(condition, f, Provided::empty);
    }

    static <T, R> Decorator<T, R> unless(Predicate<T> condition, Function<T, R> fallback) {
        return f -> Provided.decorate(condition.negate(), f, fallback);
    }

    static <T, R> R applyProvided(Predicate<T> condition,
                                  Function<T, R> function,
                                  Function<T, R> fallback,
                                  T to) {
        return Provided.decorate(condition, function, fallback).apply(to);
    }

    static <R, T> R empty(T t) {
        return null;
    }
}
