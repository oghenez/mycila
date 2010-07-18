/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.plugin.spi.internal.invoke;

import com.mycila.plugin.spi.internal.CustomClassLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class InvokablesTest {
    public String test;

    @Test
    public void test() throws Exception {
        assertTrue(InvokableFastCtor.class.isInstance(Invokables.get(getClass().getConstructor())));
        assertTrue(InvokableFastMethod.class.isInstance(Invokables.get(getClass().getMethod("test"), this)));
        assertTrue(InvokableField.class.isInstance(Invokables.get(getClass().getField("test"), this)));

        CustomClassLoader loader = CustomClassLoader.create(null, true, true)
                .add(new File("target/classes"))
                .add(new File("target/test-classes"));
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);

        try {
            Object dummy = loader.load(Funny.class.getName()).newInstance();
            assertTrue(InvokableCtor.class.isInstance(Invokables.get(dummy.getClass().getConstructor())));
            assertTrue(InvokableMethod.class.isInstance(Invokables.get(dummy.getClass().getMethod("bear"), dummy)));
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    public static class Funny {
        public void bear() {
        }
    }
}
