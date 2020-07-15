package com.scalefocus.camp.combinators.usage;

import com.scalefocus.camp.combinators.cross.Response;

public class MeasuredResponse<R> implements Response<R> {
    private Long measure;
    private R result;

    public MeasuredResponse(R result, Long measure) {
        this.measure = measure;
        this.result = result;
    }

    @Override
    public R getResult() {
        return result;
    }

    public Long getMeasure() {
        return measure;
    }

    public static <R> MeasuredResponse<R> from(R result, Long measured) {
        return new MeasuredResponse<>(result, measured);
    }

}
