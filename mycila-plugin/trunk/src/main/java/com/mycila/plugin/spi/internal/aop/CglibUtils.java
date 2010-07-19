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

package com.mycila.plugin.spi.internal.aop;

import com.mycila.plugin.Plugin;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class CglibUtils {
    private CglibUtils() {
    }

    private static final String MYCILA_PACKAGE = Plugin.class.getPackage().getName();
    private static final String CGLIB_PACKAGE = net.sf.cglib.proxy.Enhancer.class.getName().replaceFirst("\\.cglib\\..*$", ".cglib");

    private static final Map<Class<?>, WeakReference<net.sf.cglib.reflect.FastClass>> CLASSES = new WeakHashMap<Class<?>, WeakReference<net.sf.cglib.reflect.FastClass>>();

    public static net.sf.cglib.reflect.FastClass getFastClass(Class<?> c) {
        WeakReference<net.sf.cglib.reflect.FastClass> ref = CLASSES.get(c);
        net.sf.cglib.reflect.FastClass fast = null;
        if (ref != null)
            fast = ref.get();
        if (fast == null)
            CLASSES.put(c, new WeakReference<net.sf.cglib.reflect.FastClass>(fast = net.sf.cglib.reflect.FastClass.create(c)));
        return fast;
    }
}
