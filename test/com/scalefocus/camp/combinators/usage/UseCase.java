package com.scalefocus.camp.combinators.usage;

import java.util.function.Function;

public interface UseCase<T, R> extends Function<T, R> {
    static <T, R> UseCase<T, R> from(Function<T, R> f) {
        return (t) -> f.apply(t);
    }

    interface Prime extends UseCase<String, Object> {
    }

    static Prime defaultPrime() {
        return s -> s.length();
    }
}
