package com.scalefocus.camp.combinators.usage;

import com.scalefocus.camp.combinators.Decorator;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class AfterDecorator<T, R> implements Decorator<T, R> {

    public static void usage() {
        // Construct the cross-cut behavior Decorator:
        Decorator<String, Object> decorator = Decorator.after(
                afterCallback()
        );
        // Decorate the existing method and apply
        final Object result = decorator.from(
                existingMethod()
        ).apply("input");

        // Even better:
        decorator.from(UseCase.from(existingMethod())).apply("X");
    }

    private static BiConsumer<String, Object> afterCallback() {
        return (t, r) -> System.out.println("After: " + t + " -> " + r);
    }

    private static Function<String, Object> existingMethod() {
        return Function.identity().compose(String::length);
    }

}
