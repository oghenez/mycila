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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.mycila.guice.annotation.PostInject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class PostInjectionListenerTest {

    @Test
    public void test_inject_in_interceptor() throws Exception {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                MycilaBinder mycilaGuice = MycilaBinder.on(binder());
                mycilaGuice.bindPostInjectListener();
                mycilaGuice.bindInterceptor(Matchers.subclassesOf(A.class), Matchers.any(), new MethodInterceptor() {
                    @Inject
                    Injector injector;

                    @Override
                    public Object invoke(MethodInvocation invocation) throws Throwable {
                        System.out.println(invocation.getMethod());
                        assertNotNull(injector);
                        return invocation.proceed();
                    }
                });
            }
        });
        B b = injector.getInstance(B.class);
        assertSame(b, injector.getInstance(B.class));
        assertEquals("[1, 2]", b.calls.toString());
        b.intercept();
    }

    @Test
    public void test() throws Exception {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                MycilaBinder mycilaGuice = MycilaBinder.on(binder());
                mycilaGuice.bindPostInjectListener();
            }
        });
        assertEquals("[1, 2]", injector.getInstance(B.class).calls.toString());
    }

    static class A {
        List<Integer> calls = new LinkedList<Integer>();

        @Inject
        void method(B b) {
            calls.add(1);
        }

        @PostInject
        void init() {
            calls.add(2);
        }
    }

    @Singleton
    static class B extends A {
        void intercept() {}
    }
}
