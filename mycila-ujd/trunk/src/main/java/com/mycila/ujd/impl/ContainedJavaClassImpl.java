package com.mycila.ujd.impl;

import com.mycila.ujd.api.ContainedJavaClass;
import com.mycila.ujd.api.Container;

import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ContainedJavaClassImpl<T> extends JavaClassImpl<T> implements ContainedJavaClass<T> {

    ContainedJavaClassImpl(JVMImpl jvm, Class<T> theClass) {
        super(jvm, theClass);
    }

    public Container getContainer() {
        return jvm.containerRegistry.get(this);
    }

    public URL getURL() {
        return theClass.getClassLoader().getResource(getPath());
    }

    public String getPath() {
        return ClassUtils.getPath(theClass);
    }

    public String getClassName() {
        return getClass().getName();
    }
}
