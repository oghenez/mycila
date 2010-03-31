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

package com.mycila.jmx.export;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Exposures {

    private static final Map<Class<? extends JmxExposure>, Reference<JmxExposure>> cache = new WeakHashMap<Class<? extends JmxExposure>, Reference<JmxExposure>>();

    private Exposures() {
    }

    public static JmxExposure get(Class<? extends JmxExposure> exp) {
        if (exp == null) exp = AnnotationExposure.class;
        Reference<JmxExposure> ref = cache.get(exp);
        JmxExposure exposure;
        if (ref != null) {
            exposure = ref.get();
            if (exposure != null) return exposure;
        }
        try {
            exposure = exp.getConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException().getMessage(), e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        cache.put(exp, new WeakReference<JmxExposure>(exposure));
        return exposure;
    }

    public static final class AnnotationExposure implements JmxExposure {

    }
}
