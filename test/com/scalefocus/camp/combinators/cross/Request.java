package com.scalefocus.camp.combinators.cross;

/**
 * Request for a Serive/Use case with a specific Payload and meta-data.
 *
 * @param <T> - the type of the Payload
 */
@FunctionalInterface
public interface Request<T> {
    T getPayload();

    default boolean isAsync() {
        return true;
    }

    default Principal getInitiator() {
        return null;
    }

    static <P> Request<P> init(P payload) {
        return () -> payload;
    }

}
