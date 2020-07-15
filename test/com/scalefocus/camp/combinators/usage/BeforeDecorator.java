package com.scalefocus.camp.combinators.usage;

import com.scalefocus.camp.combinators.Decorator;
import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;

public class BeforeDecorator implements Decorator<String, Object> {
    @Override
    public Function<String, Object> apply(Function<String, Object> trFunction) {
        return Decorator.before(beforeCallback()).apply(trFunction);
    }

    public static void usage() {
        String input = "input";
        Object result = null;
        // Construct the cross-cut behavior Decorator:
        Decorator<String, Object> decorator = Decorator.before(
                beforeCallback()
        );
        // Decorate the existing method:

        // Apply to input:
        UseCase.of(existingMethod()).apply(input);
        log("Result: " + result);


        // One shot notation:
        log("Result: "
        );

        // Using this class implementation shorthand:
        log("Response: " + result);
    }

    private static Function<String, Object> existingMethod() {
        return Function.identity().compose(String::length);
    }

    private static Consumer<String> beforeCallback() {
        return (input) -> {
            log("Init: " + input);
        };
    }

    private static void log(String x) {
        System.out.println(x);
    }

    @Test
    public void testBeforeUsage() {
        BeforeDecorator.usage();
    }
}
