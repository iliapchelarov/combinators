package com.scalefocus.camp.combinators.cross;

public class BasicRequest implements Request<String> {
    String payload;
    Principal principal;

    public BasicRequest(String payload) {
        this.payload = payload;
        principal = Principal.create(null);
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public Principal getInitiator() {
        return principal;
    }

    public static Request<String> init(String payload, String from) {
        return new BasicRequest(payload);
    }
}
