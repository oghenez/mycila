/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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
import com.google.inject.BindingAnnotation;
import com.google.inject.Module;
import com.google.inject.Scope;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.mycila.inject.injector.AnnotatedMemberHandler;
import com.mycila.inject.injector.AnnotatedMemberHandlerTypeListener;
import com.mycila.inject.injector.FieldHandler;
import com.mycila.inject.injector.FieldHandlerTypeListener;
import com.mycila.inject.injector.KeyProvider;
import com.mycila.inject.injector.MemberInjectorTypeListener;
import com.mycila.inject.injector.MethodHandler;
import com.mycila.inject.injector.MethodHandlerTypeListener;
import com.mycila.inject.scope.ConcurrentSingleton;
import com.mycila.inject.scope.ExpiringSingleton;
import com.mycila.inject.scope.RenewableSingleton;
import com.mycila.inject.scope.ResetScope;
import com.mycila.inject.scope.ResetSingleton;
import com.mycila.inject.scope.SoftSingleton;
import com.mycila.inject.scope.WeakSingleton;
import org.aopalliance.intercept.MethodInterceptor;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaGuice {
    private final Binder binder;

    private MycilaGuice(Binder binder) {
        this.binder = binder;
    }

    public MycilaGuice bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                       Matcher<? super Method> methodMatcher,
                                       MethodInterceptor... interceptors) {
        for (MethodInterceptor interceptor : interceptors)
            inject(interceptor);
        binder.bindInterceptor(classMatcher, methodMatcher, interceptors);
        return this;
    }

    public Scope expiringSingleton(long expirity, TimeUnit unit) {
        return inject(new ExpiringSingleton(expirity, unit));
    }

    public Scope renewableSingleton(long expirity, TimeUnit unit) {
        return inject(new RenewableSingleton(expirity, unit));
    }

    public Scope weakSingleton() {
        return inject(new WeakSingleton());
    }

    public Scope softSingleton() {
        return inject(new SoftSingleton());
    }

    public Scope concurrentSingleton() {
        return inject(new ConcurrentSingleton(10, TimeUnit.SECONDS));
    }

    public Scope concurrentSingleton(long expirity, TimeUnit unit) {
        return inject(new ConcurrentSingleton(expirity, unit));
    }

    public ResetScope resetSingleton() {
        return inject(new ResetSingleton());
    }

    public <A extends Annotation> MycilaGuice bindAnnotationInjector(Class<A> annotationType, Class<? extends KeyProvider<A>> providerClass) {
        binder.bindListener(Matchers.any(), inject(new MemberInjectorTypeListener<A>(annotationType, providerClass)));
        return this;
    }

    public <A extends Annotation> MycilaGuice handleMethodAfterInjection(Class<A> annotationType, Class<? extends MethodHandler<A>> providerClass) {
        binder.bindListener(Matchers.any(), inject(new MethodHandlerTypeListener<A>(annotationType, providerClass)));
        return this;
    }

    public <A extends Annotation> MycilaGuice handleFieldAfterInjection(Class<A> annotationType, Class<? extends FieldHandler<A>> providerClass) {
        binder.bindListener(Matchers.any(), inject(new FieldHandlerTypeListener<A>(annotationType, providerClass)));
        return this;
    }

    public <A extends Annotation> MycilaGuice handleAfterInjection(Class<A> annotationType, Class<? extends AnnotatedMemberHandler<A>> providerClass) {
        binder.bindListener(Matchers.any(), inject(new AnnotatedMemberHandlerTypeListener<A>(annotationType, providerClass)));
        return this;
    }

    public <T> MycilaGuice bind(Class<T> type, T instance) {
        binder.bind(type).toInstance(inject(instance));
        return this;
    }

    public MycilaGuice install(Module module) {
        binder.install(inject(module));
        return this;
    }

    public <T> T inject(T object) {
        binder.requestInjection(object);
        return object;
    }

    /* static */

    public static MycilaGuice in(Binder binder) {
        return new MycilaGuice(binder);
    }

    /**
     * Returns true if annotations of the specified type are binding annotations.
     */
    public static boolean isBindingAnnotation(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(BindingAnnotation.class)
                || annotationType.isAnnotationPresent(Qualifier.class);
    }

}
