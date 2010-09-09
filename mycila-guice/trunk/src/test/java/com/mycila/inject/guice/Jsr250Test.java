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

package com.mycila.inject.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.Stage;
import com.google.inject.matcher.Matchers;
import com.mycila.inject.Jsr250Destroyer;
import com.mycila.inject.annotation.SoftSingleton;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

import static com.mycila.inject.guice.BinderHelper.*;
import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class Jsr250Test {

    @Test
    public void test_inject_in_interceptor() throws Exception {
        B.calls.clear();
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                install(new Jsr250Module());
                on(binder()).bindInterceptor(Matchers.subclassesOf(A.class), Matchers.any(), new MethodInterceptor() {
                    @Inject
                    Injector injector;

                    @Override
                    public Object invoke(MethodInvocation invocation) throws Throwable {
                        System.out.println("intercept: " + invocation.getMethod());
                        assertNotNull(injector);
                        return invocation.proceed();
                    }
                });
            }
        });
        B b = injector.getInstance(B.class);
        assertSame(b, injector.getInstance(B.class));
        b.intercept();
        injector.getInstance(Jsr250Destroyer.class).preDestroy();
        assertEquals("[1, 2, 3]", B.calls.toString());
    }

    @Test
    public void test() throws Exception {
        B.calls.clear();
        Injector injector = Guice.createInjector(Stage.PRODUCTION, new AbstractModule() {
            @Override
            protected void configure() {
                install(new Jsr250Module().addCloseableScopes(ExtraScope.SOFT_SINGLETON.get()));
                bindScope(SoftSingleton.class, ExtraScope.SOFT_SINGLETON.get());
                bind(C.class);
            }
        });
        injector.getInstance(C.class);
        injector.getInstance(B.class);
        assertEquals("[4, 1, 2]", B.calls.toString());
        injector.getInstance(Jsr250Destroyer.class).preDestroy();
        assertEquals("[4, 1, 2, 5, 3]", B.calls.toString());
    }

    static class A {
        static List<Integer> calls = new LinkedList<Integer>();

        @Inject
        void method(B b) {
            calls.add(1);
        }

        @PostConstruct
        void init() {
            calls.add(2);
        }

        @PreDestroy
        void close() {
            calls.add(3);
        }
    }

    @Singleton
    static class B extends A {
        void intercept() {
        }
    }

    @SoftSingleton
    static class C {
        @PostConstruct
        void init() {
            A.calls.add(4);
        }

        @PreDestroy
        void close() {
            A.calls.add(5);
        }
    }
}
