package com.scalefocus.camp.combinators.cross;

import java.util.function.Function;

/**
 * Handle Request with a given type of Payload and give a Response with a given type of Result.
 *
 * @param <P> type of the Request Payload
 * @param <R> type of the Response Result
 */
@FunctionalInterface
public interface Handler<P, R> extends Function<Request<P>, Response<R>> {

    default Response<R> handle(Request<P> request) {
        return apply(request);
    }

    static <R> Response<R> success(R result) {
        return () -> result;
    }

    static <P, R> Handler<P, R> construct(Function<P, R> useCase) {
        return (pRequest -> success(
                useCase.apply(pRequest.getPayload())
        ));
    }

}
