package com.mycila.plugin.aop;

import com.mycila.plugin.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Invokables {

    private static boolean useCglib = false;
    static {
        try {
            ClassUtils.getDefaultClassLoader().loadClass("net.sf.cglib.proxy.Callback");
            useCglib = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    private Invokables(){}

    public static <T> InvokableMember<T> get(Constructor<T> ctor) {
        return useCglib ? new InvokableFastCtor<T>(ctor) : new InvokableCtor<T>(ctor);
    }

    public static <T> InvokableMember<T> get(Method method, Object target) {
        return useCglib ? new InvokableFastMethod<T>(target, method) : new InvokableMethod<T>(target, method); 
    }

    public static <T> InvokableComposite<T> composite() {
        return new InvokableCompositeImpl<T>();
    }

}
