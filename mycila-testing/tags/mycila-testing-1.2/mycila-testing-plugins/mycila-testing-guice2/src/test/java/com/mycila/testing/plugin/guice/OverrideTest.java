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
import com.mycila.testing.testng.AbstractMycilaTestNGTest;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@GuiceContext(OverrideTest.MyModule.class)
public final class OverrideTest extends AbstractMycilaTestNGTest {

    @Inject
    String value;

    @Bind
    String a = "B";

    @Test
    public void test_overide() throws Exception {
        assertEquals(value, "B");
    }

    static final class MyModule extends AbstractModule {
        protected void configure() {
            bind(String.class).toInstance("A");
        }
    }
}
