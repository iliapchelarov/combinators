package com.scalefocus.camp.combinators.usage;

import com.scalefocus.camp.combinators.cross.*;

import java.util.function.Function;

public class TransactionalHandler<P, R> extends TransactionalDecorator<Request<P>, Response<R>>
        implements Handler<P, R> {

    private Handler<P, R> original;
    private Exception caughtException;

    public TransactionalHandler(Handler<P, R> original) {
        this.original = original;
    }

    private Handler<P, R> makeTransactionalFrom(Handler<P, R> original) {
        final Function<Request<P>, Response<R>> function = getDecorator().from(original);

        return (request -> {
            Response<R> response = function.apply(request);
            if (response == null) return ErrorResponse.with(caughtException);
            else return response;
        });
    }

    @Override
    public Response<R> apply(Request<P> pRequest) {
        return makeTransactionalFrom(original).apply(pRequest);
    }

    @Override
    public void rollback(Request<P> input, Exception e) {
        this.caughtException = e;
        super.rollback(input, e);
    }

}
