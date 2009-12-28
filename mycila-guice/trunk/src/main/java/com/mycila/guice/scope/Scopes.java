package com.mycila.guice.scope;

import com.google.inject.Scope;

import java.util.concurrent.TimeUnit;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Scopes {

    private Scopes() {}

    public static Scope cachedScope(long duration, TimeUnit unit) {
        return new CachedScope(duration, unit);
    }

}