package com.mycila.plugin.metadata.model;

import com.mycila.plugin.Invokable;
import com.mycila.plugin.metadata.InvokeException;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class InvokableMethod implements Invokable {

    private final FastMethod method;
    private final Object target;

    public InvokableMethod(Object target, FastMethod method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public Object invoke(Object... args) throws InvokeException {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw new InvokeException(method.getJavaMethod(), e.getTargetException());
        } catch (Exception e) {
            throw new InvokeException(method.getJavaMethod(), e);
        }
    }

    @Override
    public String toString() {
        return "InvokableMethod " + method.getJavaMethod();
    }
}
