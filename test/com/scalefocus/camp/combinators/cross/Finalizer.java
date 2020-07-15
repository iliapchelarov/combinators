package com.scalefocus.camp.combinators.cross;

import java.util.function.Consumer;

@FunctionalInterface
public interface Finalizer<T> extends Consumer<T> {
    default void onFinish(T t) {
        accept(t);
    }
}
