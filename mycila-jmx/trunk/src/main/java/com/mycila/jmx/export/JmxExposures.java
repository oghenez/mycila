package com.mycila.jmx.export;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JmxExposures {

    private static final Map<Class<? extends JmxExposure>, Reference<JmxExposure>> cache = new WeakHashMap<Class<? extends JmxExposure>, Reference<JmxExposure>>();

    private JmxExposures() {
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
