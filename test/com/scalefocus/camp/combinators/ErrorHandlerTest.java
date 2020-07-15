package com.scalefocus.camp.combinators;

import com.scalefocus.camp.combinators.cross.ErrorCatcher;
import com.scalefocus.camp.combinators.cross.ErrorResponse;
import com.scalefocus.camp.combinators.cross.Handler;
import com.scalefocus.camp.combinators.cross.Response;
import com.scalefocus.camp.combinators.usage.HandlerBuilder;
import com.scalefocus.camp.combinators.usage.UseCase;
import org.junit.Test;

import java.util.function.Function;
import java.util.logging.Logger;

import static com.scalefocus.camp.combinators.cross.Request.init;
import static org.assertj.core.api.Assertions.assertThat;

public class ErrorHandlerTest {
    // Step 0: Construct a Client with Logger and ability to Handle Use cases
    Logger log = Logger.getAnonymousLogger();


    // Step 1: try some invalid input produces an Error
    @Test(expected = NullPointerException.class)
    public void testHandlerError() {
        final String payload = null;
        log.info(handleDefaultUseCase(payload).toString());
    }

    // Step 2: Handle the Error around the use case and produce a meaningful (error) Response
    @Test
    public void testHandlerErrorCatch() {
        final String payload = null;
        final UseCase.Prime useCase = UseCase.defaultPrime();
        //// Prepare
        final ErrorCatcher<String, Object> catcher = new ErrorCatcher<>();
        final Function<String, Object> decorated = Decorator.around(catcher).from(useCase);
        Handler<String, Object> handler = (request) -> {
            decorated.apply(request.getPayload());
            return catcher.getResponse();
        };
        //// Invoke
        final Response<Object> response = handler.handle(init(payload));

        // Check
        assertThat(response).isNotNull();
        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.getResult()).isNull();
        assertThat(response.getError()).isNotEmpty();

        log.info("Result: " + String.valueOf(response.getResult()));
        log.info("Error: " + String.valueOf(response.getError()));
    }

    // Step 3 - abstract the Error Handler - to a HandlerBuilder and leave only the Client ability
    @Test
    public void testErrorWithHandlerBuilder() {
        final String payload = null;
        final UseCase.Prime useCase = UseCase.defaultPrime();
        final Response<Object> response = handleAnyErroneousUseCase(payload, useCase);
        assertThat(response).isInstanceOf(ErrorResponse.class);
        assertThat(response.getError()).isNotEmpty();
        assertThat(((ErrorResponse) response).getException()).isInstanceOf(NullPointerException.class);

        assertThat(
                handleAnyErroneousUseCase("X", useCase).isSuccessful()
        );
    }

    // Ability of the Client
    private Response<Object> handleAnyErroneousUseCase(String payload, Function<String, Object> useCase) {
        return HandlerBuilder.constructErrorHandler(useCase).apply(init(payload));
    }

    // Ability of the Client
    private Response<Object> handleUseCase(String payload, Function<String, Object> useCase) {
        return Handler.construct(useCase).apply(init(payload));
    }

    // Ability of the Client
    private Object handleDefaultUseCase(String payload) {
        return Handler.construct(
                UseCase.defaultPrime()
        ).handle(
                init(payload)
        ).getResult();
    }

}
