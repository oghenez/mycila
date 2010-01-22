package com.mycila.ujd.impl;

import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.JavaClassContainedLoaded;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JavaClassContainedLoadedImpl<T> extends JavaClassLoadedImpl<T> implements JavaClassContainedLoaded<T> {
    JavaClassContainedLoadedImpl(Class<T> theClass) {
        super(theClass);
    }

    public Container getContainer() {
        return null;
    }

    public String getPath() {
        return null;
    }

    public String getClassName() {
        return getClass().getName();
    }
}
