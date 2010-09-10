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

package com.mycila.inject;

import com.google.inject.Binder;
import com.google.inject.Scope;
import com.google.inject.matcher.Matcher;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class BinderHelper {
    private final Binder binder;

    private BinderHelper(Binder binder) {
        this.binder = binder;
    }

    public BinderHelper bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                        Matcher<? super Method> methodMatcher,
                                        MethodInterceptor... interceptors) {
        for (MethodInterceptor interceptor : interceptors)
            inject(interceptor);
        binder.bindInterceptor(classMatcher, methodMatcher, interceptors);
        return this;
    }

    public Scope expiringSingleton(long expirity, TimeUnit unit) {
        return inject(ExtraScopes.expiringSingleton(expirity, unit));
    }

    public Scope renewableSingleton(long expirity, TimeUnit unit) {
        return inject(ExtraScopes.renewableSingleton(expirity, unit));
    }

    public Scope weakSingleton() {
        return inject(ExtraScopes.weakSingleton());
    }

    public Scope softSingleton() {
        return inject(ExtraScopes.softSingleton());
    }

    public Scope concurrentSingleton() {
        return inject(ExtraScopes.concurrentSingleton());
    }

    public Scope concurrentSingleton(long expirity, TimeUnit unit) {
        return inject(ExtraScopes.concurrentSingleton(expirity, unit));
    }

    public static BinderHelper in(Binder binder) {
        return new BinderHelper(binder);
    }

    private <T> T inject(T object) {
        binder.requestInjection(object);
        return object;
    }

}
