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

package com.mycila.inject.util;

import com.google.inject.Binder;
import com.google.inject.matcher.Matcher;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;

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
            binder.requestInjection(interceptor);
        binder.bindInterceptor(classMatcher, methodMatcher, interceptors);
        return this;
    }

    public static BinderHelper on(Binder binder) {
        return new BinderHelper(binder);
    }
}
