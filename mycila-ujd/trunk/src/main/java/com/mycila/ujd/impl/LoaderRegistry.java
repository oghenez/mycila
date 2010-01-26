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

import com.mycila.ujd.api.JavaClass;
import com.mycila.ujd.api.Loader;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class LoaderRegistry {

    private final ConcurrentHashMap<ClassLoader, LoaderImpl> loaders = new ConcurrentHashMap<ClassLoader, LoaderImpl>();
    private final JVMImpl jvm;

    LoaderRegistry(JVMImpl jvm) {
        this.jvm = jvm;
    }

    Loader get(ClassLoader classLoader) {
        if (classLoader == null)
            classLoader = ClassLoader.getSystemClassLoader().getParent();
        if (classLoader == null)
            throw new AssertionError("Error locating bootstrap classloader !");
        LoaderImpl loader = loaders.get(classLoader);
        if (loader == null)
            loaders.put(classLoader, loader = new LoaderImpl(jvm, classLoader));
        return loader;
    }

    Loader get(JavaClass<?> theClass) {
        return get(theClass.get().getClassLoader());
    }

}