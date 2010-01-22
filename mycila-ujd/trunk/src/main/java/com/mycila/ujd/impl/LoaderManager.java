package com.mycila.ujd.impl;

import com.mycila.ujd.api.JavaClass;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class LoaderManager {
    public static <T> JavaClass<T> create(Class<T> aClass) {
        return ClassUtils.isGeneratedClass(aClass) ?
                new JavaClassLoadedImpl<T>(aClass) :
                new JavaClassContainedLoadedImpl<T>(aClass);
    }
}