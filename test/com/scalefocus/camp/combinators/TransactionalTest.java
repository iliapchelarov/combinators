package com.scalefocus.camp.combinators;

import com.scalefocus.camp.combinators.cross.*;
import com.scalefocus.camp.combinators.usage.HandlerBuilder;
import com.scalefocus.camp.combinators.usage.TransactionalHandler;
import com.scalefocus.camp.combinators.usage.UseCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Function;
import java.util.logging.Logger;

import static com.scalefocus.camp.combinators.cross.Request.init;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionalTest {

    Logger log = Logger.getAnonymousLogger();

    @Spy
    private TransactionalDecorator<String, String> trans;
    @Spy
    private TransactionalDecorator<Request<Object>, Response<Object>> transHandler;

    // Basic Use Case interaction
    @Test
    public void testUseCase() {
        final Object result = UseCase.defaultPrime().apply("X");
        assertThat(result).isInstanceOf(Integer.class);
        assertThat(result).isEqualTo(1);
        log.info(result.toString());
    }

    // Test the default Use Case with erroneous input
    @Test(expected = NullPointerException.class)
    public void testHandlerError() {
        log.info(Handler.construct(UseCase.defaultPrime()).handle(init(null)).getResult().toString());
    }

    @Test
    public void testTransactionalCommit() {
        final Function<String, String> handler = trans.getDecorator().from(
                (x) -> "called"
        );
        handler.apply("X");

        verify(trans).begin();
        verify(trans).commit("X", "called");
        verify(trans).finish("X", "called");
        verify(trans, times(0)).rollback(any(), any());
    }

    @Test
    public void testTransactionalRollback() {
        final Function<String, String> handler = trans.getDecorator().from(
                (x) -> x.toUpperCase()
        );
        handler.apply(null);

        verify(trans).begin();
        verify(trans, times(0)).commit(any(), any());
        verify(trans).finish(isNull(), any());
        verify(trans).rollback(isNull(), any());
    }

    // Step 2: Wrap the transactional behaviour around an ordinary handler
    @Test
    public void testTransactionalSuccessHandler() {
        final Request<Object> request = init("input");
        final Response<Integer> success = new TransactionalHandler<>(
                Handler.construct(payload -> payload.toString().length())
        ).handle(request);

        assertThat(success).isNotNull();
        assertThat(success.isSuccessful());
        assertThat(success.getResult()).isNotNull();
    }

    // Step 2: Wrap the transactional behaviour with naked Decorator for erroneous handler
    @Test
    public void testTransactionalDecoRollback() {
        final Request<Object> request = init(null);
        final Function<Object, Object> useCase = payload -> payload.toString().toUpperCase();
        //// Construct
        Function<Request, Response> transactionalApplication = (pRequest) -> Around.applyAround(
                Handler.construct(useCase),
                transHandler,
                pRequest);
        //// Invoke
        final Response<Object> error = transactionalApplication.apply(request);

        // Check - Exception must mean null response, since the original Use Case did not compute.
        assertThat(error).isNull();
        verify(transHandler).rollback(eq(request), isA(NullPointerException.class));
        verify(transHandler, times(0)).commit(any(), any());
    }

    // Step 2: Wrap the transactional behaviour with naked Decorator for erroneous handler
    @Test
    public void testTransactionalHandlerRollbackNull() {
        final Function<Object, Object> useCase = payload -> payload.toString().toUpperCase();

        final Handler<Object, Object> handler = TransactionalDecorator.decorate(Handler.construct(useCase));

        // Check - Exception must mean null response, since the original Use Case did not compute.
        assertThat(handler.handle(init(null))).isNull();
        assertThat(handler.handle(init("X"))).isNotNull();
//        assertThat(error).isNotNull();
//        assertThat(error.isSuccessful()).isFalse();
//        assertThat(error.getResult()).isNull();

    }

    // Step 2: Wrap the transactional behaviour with naked Decorator for erroneous handler
    @Test
    public void testTransactionalHandlerRollbackKeepException() {
        final Function<Object, Object> useCase = payload -> payload.toString().toUpperCase();

        final TransactionalHandler<Object, Object> handler = spy(new TransactionalHandler<>(
                Handler.construct(useCase)
        ));

        // Check - Exception must be returned as Response, even though original Use Case did not compute.
        final Response<Object> error = handler.handle(init(null));
        assertThat(error).isNotNull();
        assertThat(error.isSuccessful()).isFalse();
        assertThat(error.getResult()).isNull();
        assertThat(error).isInstanceOf(ErrorResponse.class);
        assertThat(error.getError()).contains("NullPointerException");
        verify(handler).rollback(any(Request.class), any(NullPointerException.class));
        verify(handler, times(0)).commit(any(), any());

        final Response<Object> success = handler.handle(init("x"));
        assertThat(success).isNotNull();
        assertThat(success.isSuccessful()).isTrue();
        assertThat(success.getResult()).isEqualTo("X");
        assertThat(success).isNotInstanceOfAny(ErrorResponse.class);
        assertThat(success.getError()).isNull();
        verify(handler, times(1)).rollback(any(Request.class), any(NullPointerException.class));
        verify(handler, times(1)).commit(any(Request.class), eq(success));
    }

    // Step 3: Abstract the Decorator & Handler construction - to the Handler Builder
    @Test
    public void testHandlerBuilder() {
        constructTransactionalHandler(Handler.construct(o -> o));
    }

    Handler<String, Object> constructTransactionalHandler(Handler<String, Object> original) {
        return null;
        //TODO implement here
    }

    // Ability of the Client - to submit Request to Handler and get Response
    @Test
    public void testHandler() {
        Handler.<String, Integer>construct(s -> s.length());
        log.info(Handler.construct(UseCase.defaultPrime()).handle(init("X")).getResult().toString());
    }

    // Ability of the Client
    private Response<Object> handleAnyTransactionalUseCase(String payload, Function<String, Object> useCase) {
        return HandlerBuilder.constructTransactionalHandler(
                Handler.construct(useCase)
        ).handle(init(payload));
    }

}
