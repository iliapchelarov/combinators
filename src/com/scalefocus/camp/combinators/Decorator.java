package com.scalefocus.camp.combinators;

import java.util.concurrent.Callable;
import java.util.function.*;

@FunctionalInterface
public interface Decorator<T, R> extends UnaryOperator<Function<T, R>> {

    static <T, R> Decorator<T, R> before(Consumer<T> before) {
        return Before.before(before);
    }

    static <T, R> Decorator<T, R> after(BiConsumer<T, R> after) {
        return After.after(after);
    }

    static <T, R> Decorator<T, R> around(BiConsumer<Callable<R>, T> around) {
        return Around.around(around);
    }

    static <T, R> Decorator<T, R> provided(Predicate<T> condition) {
        return Provided.provided(condition);
    }

    static <T, R> Decorator<T, R> provided(Predicate<T> condition, Function<T, R> fallback) {
        return Provided.provided(condition, fallback);
    }

    static <T, R> Decorator<T, R> precondition(Predicate<T> condition) {
        return Precondition.precondition(condition);
    }

    static <T, R> Decorator<T, R> postcondition(BiPredicate<T, R> condition) {
        return Postcondition.postcondition(condition);
    }

    default <F extends Function<T, R>> Function<T, R> from(F f) {
        return apply(f);
    }
}
