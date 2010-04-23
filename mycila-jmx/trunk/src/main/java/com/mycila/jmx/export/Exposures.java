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

import com.mycila.jmx.export.annotation.JmxBean;
import com.mycila.jmx.util.ExceptionUtils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Exposures {

    private static final Map<Class<? extends JmxExposure>, Reference<JmxExposure>> cache = new WeakHashMap<Class<? extends JmxExposure>, Reference<JmxExposure>>();

    private Exposures() {
    }

    public static JmxExposure get(Class<?> mbeanClass) {
        JmxBean jmxBean = mbeanClass.getAnnotation(JmxBean.class);
        if (jmxBean == null)
            throw new IllegalArgumentException("No @JmxBean annotation on class hierarchy: " + mbeanClass.getName());
        return load(jmxBean.exposure());
    }

    private static JmxExposure load(Class<? extends JmxExposure> exp) {
        if (exp == null) exp = AnnotationMetadataAssembler.class;
        Reference<JmxExposure> ref = cache.get(exp);
        JmxExposure exposure;
        if (ref != null) {
            exposure = ref.get();
            if (exposure != null)
                return exposure;
        }
        try {
            exposure = exp.getConstructor().newInstance();
        } catch (Throwable e) {
            throw ExceptionUtils.rethrow(e);
        }
        cache.put(exp, new WeakReference<JmxExposure>(exposure));
        return exposure;
    }

}
