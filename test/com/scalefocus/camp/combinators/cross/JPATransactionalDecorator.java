package com.scalefocus.camp.combinators.cross;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;

public class JPATransactionalDecorator<T, R> extends TransactionalDecorator<T, R> {

    private static EntityManagerFactory emf;
    @PersistenceContext
    EntityManager em;

    public JPATransactionalDecorator() {
        initEntityManager();
        em = emf.createEntityManager();
    }

    protected static void initEntityManager() {
        if (emf == null) {
            emf =
                    Persistence.createEntityManagerFactory("h2");
        }
    }

    public final BiConsumer<Callable<R>, T> aroundActualMethod = (function, input) -> {
        R result = null;
        try {
            this.begin();
            result = function.call();
            this.commit(input, result);
        } catch (Exception e) {
            this.rollback(input, e);
        } finally {
            this.finish(input, result);
        }
    };

    BiConsumer<T, R> commit;
    BiConsumer<T, R> rollback;
    BiConsumer<T, R> finalizer;

    public void finish(T input, R result) {
        em.clear();
    }

    public void rollback(T input, Exception e) {
        e.printStackTrace();
        em.getTransaction().rollback();
    }

    public void commit(T input, R result) {
        em.getTransaction().commit();
    }

    public void begin() {
        em.getTransaction().begin();
    }
}
