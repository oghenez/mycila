/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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
package com.mycila.testing.plugin.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mycila.testing.core.MycilaTesting;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
// You can optionnaly specify some modules to be loaded, with a stage
@GuiceContext(InjectInBeanTest.MyModule.class)
public final class InjectInBeanTest {

    // you can provide bindings in your tests
    @Bind
    A a = new A();

    // You can specify annoated bindings
    @Bind(annotatedBy = Named.class)
    B b = new B();

    // You can even bind a method, and you can specify scopes
    @Bind(scope = Singleton.class)
    C create() {return new C();}

    // You can inject all what is binded, thus the injector
    @Inject Injector injector;
    @Inject D d;

    @BeforeClass
    public void setup() {
        MycilaTesting.from(getClass()).createNotifier(this).prepare();
    }

    @Test
    public void test_bind() {
        assertEquals(b.a, a);

        assertEquals(d.a, a);
        assertEquals(d.b, b);
        assertNotNull(d.c);

        B b = injector.getInstance(B.class);
        assertEquals(b.a, a);

        C c = injector.getInstance(C.class);
        assertEquals(c.a, a);
        assertEquals(c.b, this.b);
    }

    // You can also
    // - Provide Modules by methods using @ModuleProvider
    // - Make your test implement Module, to add and configure quickly a module to add to the injector
    // - Integrate with other Mycila Plugins, like JMock, EasyMock, Mockito, ... to be able to Bind mocked objects in the injector
    // - ... and many more ;)

    static class MyModule extends AbstractModule {
        protected void configure() {
            binder().bind(D.class).toInstance(new D());
        }
    }

    static class A {}
    static class B {
        @Inject A a;
    }
    static class C {
        @Inject A a;
        @Inject @Named("") B b;
    }
    static class D {
        @Inject A a;
        @Inject @Named("") B b;
        @Inject C c;
    }
}
