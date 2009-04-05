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
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.mycila.testing.core.TestSetup;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@GuiceContext(AModule.class)
public final class GuiceContextTest {

    @Inject
    @Named("service1")
    Service service1;

    @Inject
    @Named("service1")
    Provider<Service> service1Provider;

    @Inject
    Injector injector;

    @Test
    public void test_setup() {
        assertNull(service1);
        assertNull(service1Provider);
        TestSetup.staticDefaultSetup().prepare(this);
        assertNotNull(service1);
        assertNotNull(service1Provider);
        assertEquals(injector.getBindings().size(), 5);
    }

    @Test
    public void test_no_modules() {
        AClass instance = new AClass();
        assertNull(instance.injector);
        TestSetup.staticDefaultSetup().prepare(instance);
        assertNotNull(instance.injector);
        assertEquals(instance.injector.getBindings().size(), 4);
    }

    @GuiceContext()
    private static class AClass {
        @Inject
        Injector injector;
    }
}
