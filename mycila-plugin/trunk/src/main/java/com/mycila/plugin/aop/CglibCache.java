package com.mycila.plugin.aop;

import net.sf.cglib.reflect.FastClass;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class CglibCache {
    private CglibCache() {
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
