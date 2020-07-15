package com.scalefocus.camp.combinators;

import com.scalefocus.camp.combinators.cross.Finalizer;
import com.scalefocus.camp.combinators.cross.Handler;
import com.scalefocus.camp.combinators.cross.Response;
import com.scalefocus.camp.combinators.usage.HandlerBuilder;
import com.scalefocus.camp.combinators.usage.UseCase;
import org.junit.Test;

import java.util.logging.Logger;

import static com.scalefocus.camp.combinators.cross.Request.init;
import static org.assertj.core.api.Assertions.assertThat;

public class PerformanceHandlerTest {

    private static Logger log = Logger.getLogger("Performance");

    @Test
    public void testPerformanceHandler() {
        Response<Object> response = HandlerBuilder.constructPerformanceHandler(
                x -> {
                    for (int i = 0; i < 2000; i++) {
                        for (int j = 0; j < i; j++) {
                            System.out.print(Math.random() * i * j);
                        }
                        System.out.println();
                    }
                    return "";
                }
        ).handle(init(new String[]{""}));

        assertThat(response).isNotNull();
        assertThat(response.isSuccessful());
        assertThat(response.getResult()).isInstanceOf(String.class);
//        assertThat(response).isInstanceOf(MeasuredResponse.class);
//        assertThat(((MeasuredResponse) response).getMeasure()).isGreaterThan(0);
//
//        log.info("Finished in: " + ((MeasuredResponse) response).getMeasure());
    }

    @Test
    public void testPerformanceConsumer() {
        final UseCase<Object, Object> computation = x -> {
            for (int i = 0; i < 2000; i++) {
                for (int j = 0; j < i; j++) {
                    System.out.print(Math.random() * i * j);
                }
                System.out.println();
            }
            return "";
        };

        //TODO implement and test here.
        HandlerBuilder.constructPerformanceHandler(
                computation,
                (l) -> log.info("Finished with the measure: " + l)
        ).handle(init(new String[]{""}));

    }

    private <R> Handler constructPerformanceHandler(UseCase<Object, Object> computation, Finalizer<Long> monitor) {
        return HandlerBuilder.constructPerformanceHandler(computation, monitor);
    }

    ////////////////////////
//
//        Response<Object> response =
//                .constructPerformanceHandler(computation)
//                .after((request, resp) -> log.info("Finished with measure: " + ((MeasuredResponse)resp).getMeasure()))
//                .handle(Handler.init(new String[]{""}));


}

