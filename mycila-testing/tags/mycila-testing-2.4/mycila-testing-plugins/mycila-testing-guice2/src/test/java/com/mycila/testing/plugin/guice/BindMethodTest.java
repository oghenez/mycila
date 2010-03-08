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

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mycila.testing.core.MycilaTesting;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@GuiceContext(AModule.class)
public final class BindMethodTest {

    String a = "helloa";
    String b = "hellob";

    @Inject
    Injector injector;

    @Bind
    String a() {
        return a;
    }

    @Bind(annotatedBy = Named.class, scope = Singleton.class)
    String b() {
        return b;
    }

    @Bind
    ServiceImpl2 impl1() {
        return new ServiceImpl2("impl1");
    }

    @Bind(annotatedBy = Named.class)
    Service impl2() {
        return new ServiceImpl2("impl2");
    }

    @Test
    public void test_bind() {
        MycilaTesting.from(getClass()).createNotifier(this).prepare();
        assertEquals(injector.getInstance(String.class), "helloa");
        assertEquals(injector.getInstance(Key.get(String.class, Named.class)), "hellob");
        b = "changedb";
        a = "changeda";
        assertEquals(injector.getInstance(String.class), "changeda");
        assertEquals(injector.getInstance(Key.get(String.class, Named.class)), "hellob");
        assertEquals(injector.getInstance(ServiceImpl2.class).go(), "impl1");
        assertEquals(injector.getInstance(Key.get(Service.class, Named.class)).go(), "impl2");
    }
}
