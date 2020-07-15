package com.scalefocus.camp.combinators.cross;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;

public class ErrorCatcher<T, R> implements BiConsumer<Callable<R>, T> {
    R result = null;
    Exception exception = null;

    @Override
    public void accept(Callable<R> rCallable, T tInput) {
        try {
            result = rCallable.call();
        } catch (Exception e) {
            exception = e;
        }
    }

    public R getTheResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isOK() {
        return exception == null;
    }

    public Response<R> getResponse() {
        return isOK() ?
                () -> getTheResult() :
                ErrorResponse.<R>with(getException());

//        return new Response<R>() {
//            @Override
//            public R getResult() {
//                return getTheResult();
//            }
//
//            @Override
//            public String getError() {
//                return isOK()? null: getException().toString();
//            }
//
//            @Override
//            public boolean isSuccessful() {
//                return isOK();
//            }
//        };
    }
}
