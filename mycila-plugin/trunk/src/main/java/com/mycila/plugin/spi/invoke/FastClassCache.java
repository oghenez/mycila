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

package com.mycila.plugin.spi.invoke;

import net.sf.cglib.reflect.FastClass;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class FastClassCache {
    private FastClassCache() {
    }

    static final Map<Class<?>, WeakReference<FastClass>> CLASSES = new WeakHashMap<Class<?>, WeakReference<FastClass>>();

    public static FastClass getFastClass(Class<?> c) {
        WeakReference<FastClass> ref = CLASSES.get(c);
        FastClass fast = null;
        if (ref != null)
            fast = ref.get();
        if (fast == null)
            CLASSES.put(c, new WeakReference<FastClass>(fast = FastClass.create(c)));
        return fast;
    }
}
