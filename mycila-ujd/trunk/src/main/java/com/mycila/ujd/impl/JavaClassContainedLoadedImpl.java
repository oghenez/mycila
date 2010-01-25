package com.mycila.ujd.impl;

import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.JavaClassContainedLoaded;

import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JavaClassContainedLoadedImpl<T> extends JavaClassLoadedImpl<T> implements JavaClassContainedLoaded<T> {

    JavaClassContainedLoadedImpl(JVMImpl jvm, Class<T> theClass) {
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
