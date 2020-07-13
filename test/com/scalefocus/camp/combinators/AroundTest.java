package com.scalefocus.camp.combinators;

import com.scalefocus.camp.combinators.usage.UseCase;
import org.junit.Test;

public class AroundTest {

    @Test
    public void testAroundPerformanceMeasure() {
        final Double[] payload = {
                2064.1200934875,
                784.011928374,
                246.123481347650,
                5035.1233948756,
                18765.137465,
                6548.234465
        };
        final UseCase<Double[], Double> useCase = (args) -> this.existingComputation(args);

        final Double result = getPerformanceDecorator().from(
                useCase
        ).apply(payload);
    }

    public Decorator<Double[], Double> getPerformanceDecorator() {
        return Decorator.around(
                (func, t) -> {
                    long start = System.currentTimeMillis();
                    log("Starting  at: " + start);
                    Object o = null;
                    try {
                        o = func.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        long time = System.currentTimeMillis() - start;
                        log("Finished computation in " + time + "ms");
                        log("With result: " + o);
                    }
                }
        );
    }

    public Double existingComputation(Double... args) {
        double result = 0.0;
        for (double d : args) {
            final double v = Math.sin(d) * 1608.17346508173465;
            log(v);
            result = Math.pow(v, d);
            log(result);
        }
        return result;
    }

    public static void log(Object o) {
        System.out.println(o);
    }
}
