package com.scalefocus.camp.combinators.cross;

/**
 * Error Response replaces Result with an Exception
 *
 * @param <R> - for compliance. In this type - always null.
 */
@FunctionalInterface
public interface ErrorResponse<R> extends Response<R> {
    @Override
    default R getResult() {
        return null;
    }

    Exception getException();

    @Override
    default String getError() {
        return "Error: " + getException().toString() +
                "\n -- Check the Exception object for more details.";
    }

    @Override
    default boolean isSuccessful() {
        return false;
    }

    static <R> ErrorResponse<R> with(Exception e) {
        return () -> e;
    }
}
