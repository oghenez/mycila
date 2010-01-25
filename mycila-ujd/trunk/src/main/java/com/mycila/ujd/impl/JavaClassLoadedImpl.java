package com.mycila.ujd.impl;

import com.mycila.ujd.api.JavaClass;
import com.mycila.ujd.api.LoadedClass;
import com.mycila.ujd.api.Loader;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class JavaClassLoadedImpl<T> implements JavaClass<T>, LoadedClass {

    protected final Class<T> theClass;
    protected final JVMImpl jvm;
    private final int hashCode;

    JavaClassLoadedImpl(JVMImpl jvm, Class<T> theClass) {
        this.jvm = jvm;
        this.theClass = theClass;
        this.hashCode = 31 * theClass.hashCode() + theClass.getClassLoader().hashCode();
    }

    public final Loader getLoader() {
        return jvm.loaderRegistry.get(this);
    }

    public final Class<T> get() {
        return theClass;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaClassLoadedImpl that = (JavaClassLoadedImpl) o;
        return get().equals(that.get()) && get().getClassLoader().equals(that.get().getClassLoader());
    }

    @Override
    public final int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return get().getName() + "@" + Integer.toHexString(hashCode);
    }
}
