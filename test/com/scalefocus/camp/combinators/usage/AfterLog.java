package com.scalefocus.camp.combinators.usage;

import com.scalefocus.camp.combinators.Decorator;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class AfterLog<T, R> implements BiConsumer<T, R> {

    @Override
    public void accept(T t, R r) {
        log(t, r);
    }

    void log(T t, R r) {
        System.out.println("The result from (" + t + ") = " + r);
    }

    public Function<T, R> applyAfter(Function<T, R> trFunction) {
        return Decorator.after(this).from(trFunction);
    }

    public static void usage() {
        final Function<String, Object> mainMethod = Function.identity().compose(String::length);
        new AfterLog().applyAfter(
                mainMethod
        ).apply("input");
    }
}
