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

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class Tester {
    public static void main(String[] args) {
        Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(A.class).to(B.class);
                binder.bindListener(Matchers.any(), new TypeListener() {
                    @Override
                    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                        System.out.println(type);
                    }
                });
                binder.bindInterceptor(Matchers.any(), Matchers.any(), new MethodInterceptor() {
                    @Override
                    public Object invoke(MethodInvocation invocation) throws Throwable {
                        return invocation.proceed();
                    }
                });
            }
        }).getInstance(A.class);
    }

    static interface A {
    }

    static class B implements A {
        @Inject
        Injector injector;
    }
}
