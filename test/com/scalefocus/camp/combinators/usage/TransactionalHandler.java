package com.scalefocus.camp.combinators.usage;

import com.scalefocus.camp.combinators.cross.Handler;
import com.scalefocus.camp.combinators.cross.Request;
import com.scalefocus.camp.combinators.cross.Response;
import com.scalefocus.camp.combinators.cross.TransactionalDecorator;

import java.util.function.Function;

public class TransactionalHandler<P, R> extends TransactionalDecorator<Request<P>, Response<R>>
        implements Handler<P, R> {

    private Handler<P, R> original;

    public TransactionalHandler(Handler<P, R> original) {
        this.original = original;
    }

    private Handler<P, R> makeTransactionalFrom(Handler<P, R> original) {
        final Function<Request<P>, Response<R>> function = getDecorator().from(original);
        //TODO Implement here
        return null;
    }

    @Override
    public Response<R> apply(Request<P> pRequest) {
        return makeTransactionalFrom(original).apply(pRequest);
    }

}
