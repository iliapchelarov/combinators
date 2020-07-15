package com.scalefocus.camp.combinators.cross;

public class BasicResponse implements Response<Object> {
    private Object result;
    private String error;

    public BasicResponse(Object result) {
        this(result, null);
    }

    public BasicResponse(Object result, String error) {
        this.result = result;
        this.error = error;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public boolean isSuccessful() {
        return error == null && result != null;
    }

    @Override
    public String getError() {
        return error;
    }
}
