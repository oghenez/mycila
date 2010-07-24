/**
 * Copyright (C) 2010 mycila.com <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.guice;

import com.google.inject.Binder;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.mycila.guice.annotation.ExpiringSingleton;
import com.mycila.guice.annotation.SoftSingleton;
import com.mycila.guice.annotation.WeakSingleton;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaBinder {
    private final Binder binder;

    private MycilaBinder(Binder binder) {
        this.binder = binder;
    }

    public void bindPostInjectListener() {
        binder.bindListener(Matchers.any(), new PostInjectionListener());
    }

    public void bindScopes() {
        binder.bindScope(ExpiringSingleton.class, Scopes.EXPIRING_SINGLETON);
        binder.bindScope(WeakSingleton.class, Scopes.WEAK_SINGLETON);
        binder.bindScope(SoftSingleton.class, Scopes.SOFT_SINGLETON);
    }

    public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                MethodInterceptor... interceptors) {
        for (MethodInterceptor interceptor : interceptors)
            binder.requestInjection(interceptor);
        binder.bindInterceptor(classMatcher, methodMatcher, interceptors);
    }

    public static MycilaBinder on(Binder binder) {
        return new MycilaBinder(binder);
    }
}
