package com.scalefocus.camp.combinators;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public class BeforeTest {

    @Test
    public void testBeforeFunction() {
        Float[] floats = {1.2f, 0.8f};
        Function<Float[], Float> decorated = Before.decorate(this::beforePrinter, x -> computeTotal(x[0], x[1]));
        System.out.println("Invocation: " + decorated.apply(floats));

        String email = "i.pchelarov@gmail.com";
        applyBeforeAddCustomer(this::beforePrinter, email);

    }

    private void execute(Function<Object, Void> f, Object arg) {
        f.apply(arg);
    }

    private Object applyBeforeAddCustomer(Consumer<Object> before, String email) {
        return Before.decorate(
                // Before
                before,
                // Actual operator function (convert args)
                (arg) -> {
                    this.addCustomer(String.valueOf(arg));
                    return null;
                }).apply(email);
    }

    public void beforePrinter(Object... args) {
        System.out.println("Before: ");
        Arrays.asList(args).forEach((x) -> System.out.print(x + " "));
        System.out.println("\n---");
    }

    public Float computeTotal(Float price, Float quantity) {
        return price * quantity;
    }

    public void addCustomer(String email) {
        // nothing happens, internal activity, nothing is returned...
    }
}
