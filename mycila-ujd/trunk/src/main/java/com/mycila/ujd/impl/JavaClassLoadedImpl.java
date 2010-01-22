package com.mycila.ujd.impl;

import com.mycila.ujd.api.JavaClass;
import com.mycila.ujd.api.LoadedClass;
import com.mycila.ujd.api.Loader;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class JavaClassLoadedImpl<T> implements JavaClass<T>, LoadedClass {

    private final Class<T> theClass;
    private final LoaderImpl loader;

    JavaClassLoadedImpl(Class<T> theClass) {
        this.theClass = theClass;
        this.loader = new LoaderImpl(theClass.getClassLoader());
    }

    public Loader getLoader() {
        return null;
    }

    public Class<T> get() {
        return theClass;
    }
}
