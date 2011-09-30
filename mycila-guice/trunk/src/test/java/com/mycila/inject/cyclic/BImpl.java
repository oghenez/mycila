package com.mycila.inject.cyclic;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;

public class BImpl implements B {
    @Inject
    private Provider<A> a;

    private boolean called = false;

    @PostConstruct
    public void init() {
        a.get().callA();
    }

    @Override
    public void callB() {
        called = true;
    }

    public boolean hasBeenCalled() {
        return called;
    }
}
