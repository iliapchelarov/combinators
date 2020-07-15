package com.scalefocus.camp.combinators.usage;

public interface Client {
    UseCase getUseCase();

    static Client forThe(UseCase useCase) {
        return () -> useCase;
    }
}
