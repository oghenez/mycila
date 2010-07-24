package com.mycila.guice;

import com.google.inject.Binder;
import com.google.inject.matcher.Matchers;
import com.mycila.guice.annotation.ExpiringSingleton;
import com.mycila.guice.annotation.SoftSingleton;
import com.mycila.guice.annotation.WeakSingleton;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaGuice {

    private MycilaGuice() {
    }

    public static void bindPostInjectListener(Binder binder) {
        binder.bindListener(Matchers.any(), new PostInjectionListener());
    }

    public static void bindScopes(Binder binder) {
        binder.bindScope(ExpiringSingleton.class, Scopes.EXPIRING_SINGLETON);
        binder.bindScope(WeakSingleton.class, Scopes.WEAK_SINGLETON);
        binder.bindScope(SoftSingleton.class, Scopes.SOFT_SINGLETON);
    }

}
