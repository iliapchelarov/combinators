package com.scalefocus.camp.combinators.usage;

import com.scalefocus.camp.combinators.Decorator;
import com.scalefocus.camp.combinators.cross.*;

import java.util.function.Function;

public class HandlerBuilder<P, R> {


    public static <P, R> Handler<P, R> constructTransactionalHandler(Handler<P, R> original) {
        return (request) -> HandlerBuilder.<P, R>createTransactional().getDecorator().from(original).apply(request);
    }

    private static <P, R> TransactionalDecorator<Request<P>, Response<R>> createTransactional() {
        return new TransactionalDecorator<>();
    }

    public static <P, R> Handler<P, R> constructErrorHandler(Function<P, R> useCase) {
        final ErrorCatcher<P, R> catcher = new ErrorCatcher<>();
        final Function<P, R> catching = Decorator.around(catcher).from(useCase);
        return pRequest -> {
            catching.apply(pRequest.getPayload());
            return catcher.getResponse();
        };
    }

    public static <P> Handler<P, Object> constructPerformanceHandler(UseCase<P, P> useCase) {
        return constructPerformanceHandler(useCase, null);
    }

    public static <P> Handler<P, Object> constructPerformanceHandler(UseCase<P, P> useCase, Finalizer<Long> monitor) {
        final Performance performance = Performance.getInstance(monitor);
        final Function measuring = performance.measuring().from(useCase);
        return
                (pRequest) -> {
                    final Object result = measuring.apply(pRequest.getPayload());
                    return MeasuredResponse.from(result, performance.getMeasured());
                };
    }
}
