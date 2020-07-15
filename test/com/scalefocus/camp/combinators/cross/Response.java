package com.scalefocus.camp.combinators.cross;

/**
 * Response from a Serive/Use case with a specific Result and meta-data.
 *
 * @param <R>
 */
public interface Response<R> {

    R getResult();

    default boolean isSuccessful() {
        return true;
    }

    default String getError() {
        return null;
    }

    static Response<Object> success(Object result) {
        return new BasicResponse(result);
    }

    static Response<Object> error(String message) {
        return new BasicResponse(null, message);
    }
}
