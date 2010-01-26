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

package com.mycila.ujd.impl;

import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.JVMUpdater;

import java.util.Arrays;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultJVMUpdater implements JVMUpdater {

    private final JVMImpl jvm = new JVMImpl();

    public JVM get() {
        return jvm;
    }

    public JVMUpdater addClasses(Class<?>... classes) {
        return addClasses(Arrays.asList(classes));
    }

    public JVMUpdater addClasses(Iterable<Class<?>> classes) {
        for (Class<?> aClass : classes)
            if (!aClass.isArray() // ignore arrays
                    && aClass.getClassLoader() != null) // ignore classes loaded by bootstrap classloader
                jvm.classRegistry.add(aClass);
        return this;
    }
}
