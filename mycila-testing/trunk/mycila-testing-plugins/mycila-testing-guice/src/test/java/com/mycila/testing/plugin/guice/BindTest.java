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
import com.mycila.testing.core.TestSetup;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@GuiceContext(AModule.class)
public final class BindTest {

    @Bind
    String a = "hello";

    @Bind(Service.class)
    ServiceImpl2 impl1 = new ServiceImpl2("impl1");

    @Bind(value = Service.class, annotatedBy = Test.class)
    Service impl2 = new ServiceImpl2("impl2");

    @Inject
    Injector injector;

    @Test
    public void test_bind() {
        TestSetup.setup(this);
        assertEquals(injector.getBinding(Key.get(String.class)), "hello");
    }
}