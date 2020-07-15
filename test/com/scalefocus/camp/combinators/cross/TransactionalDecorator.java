package com.scalefocus.camp.combinators.cross;

import com.scalefocus.camp.combinators.Around;
import com.scalefocus.camp.combinators.Decorator;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class TransactionalDecorator<T, R> implements BiConsumer<Callable<R>, T> {

    protected static Logger log = Logger.getLogger("Transactional Behavior");

    private void transaction(Callable<R> function, T input) {
        R result = null;
        try {
            begin();
            result = function.call();
            commit(input, result);
        } catch (Exception e) {
            rollback(input, e);
        } finally {
            finish(input, result);
        }
    }

    public void accept(Callable<R> function, T input) {
        transaction(function, input);
    }

    public Decorator<T, R> getDecorator() {
        return Decorator.around(this);
    }

    public static <P, R> Handler<P, R> decorate(Handler<P, R> original) {
        return pRequest -> Around.applyAround(original, new TransactionalDecorator<>(), pRequest);
    }

    public void begin() {
    }

    public void finish(T input, R result) {
        log.info("Exiting transaction, with result: " + result);
    }

    public void rollback(T input, Exception e) {
        log.warning("Rollback transaction, due to: " + e.toString());
    }

    public void commit(T input, R result) {
        log.info(this + " Commited transaction with: " + result);
    }
}
