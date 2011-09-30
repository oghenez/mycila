package com.mycila.inject.cyclic;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;

public class AImpl implements A {
    //@Inject
    private Provider<B> b;
    private boolean called = false;

    @Inject
    public AImpl(Provider<B> b)
    //public void setB(B b)
    {
        this.b = b;
    }

    @PostConstruct
    public void init() {
        b.get().callB();
    }

    @Override
    public void callA() {
        called = true;
    }

    public boolean hasBeenCalled() {
        return called;
    }
}
