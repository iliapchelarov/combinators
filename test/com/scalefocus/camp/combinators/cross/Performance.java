package com.scalefocus.camp.combinators.cross;

import com.scalefocus.camp.combinators.Decorator;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.ToLongBiFunction;

public class Performance implements BiConsumer<Callable<Object>, Object> {

    private long measured;
    private Finalizer<Long> performanceMonitor = (l) -> this.measured = l;

    static <T, R> ToLongBiFunction<Callable<R>, T> measure() {
        return (func, t) -> {
            long start = System.currentTimeMillis();
            Object o = null;
            try {
                o = func.call();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                final long l = System.currentTimeMillis() - start;
                return l;
            }
        };
    }

    @Override
    public void accept(Callable rCallable, Object t) {
        this.measured = Performance.measure().applyAsLong(rCallable, t);
        if (performanceMonitor != null) performanceMonitor.accept(this.measured);
    }

    public Decorator measuring() {
        return Decorator.around(
                this
//                (func, x) -> Performance.<T, R>measure().applyAsLong(func, x)
        );
    }

    public static Performance getInstance() {
        return getInstance(null);
    }

    public static Performance getInstance(Finalizer<Long> observer) {
        final Performance performance = new Performance();
        if (observer != null) {
            performance.performanceMonitor = observer;
        }
        return performance;
    }

    public long getMeasured() {
        return measured;
    }
}
