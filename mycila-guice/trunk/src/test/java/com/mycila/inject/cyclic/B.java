package com.mycila.inject.cyclic;

public interface B {
    public void callB();

    public boolean hasBeenCalled();
}