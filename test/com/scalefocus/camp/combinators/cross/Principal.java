package com.scalefocus.camp.combinators.cross;

import com.scalefocus.camp.combinators.usage.UseCase;
import com.sun.net.httpserver.HttpPrincipal;

@FunctionalInterface
public interface Principal {
    String getUniqueName();

    default boolean hasAccessTo(UseCase useCase) {
        return true;
    }

    default java.security.Principal getThePrincipal() {
        return new HttpPrincipal(getUniqueName(), "org.combinators");
    }

    static Principal create(String userName) {
        return new Principal() {
            @Override
            public String getUniqueName() {
                return this.toString() + " " + userName;
            }
        };
    }
}
