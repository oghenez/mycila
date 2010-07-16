/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.plugin.spi.internal.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ClassUtils {

    private ClassUtils() {
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            return Object[].class;
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else {
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
        }
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ignored) {
        }
        if (cl == null)
            cl = ClassUtils.class.getClassLoader();
        return cl;
    }

    /**
     * Convert a "/"-based resource path to a "."-based fully qualified class name.
     *
     * @param resourcePath the resource path pointing to a class
     * @return the corresponding fully qualified class name
     */
    public static String convertResourcePathToClassName(String resourcePath) {
        Assert.notNull(resourcePath, "Resource path must not be null");
        return resourcePath.replace('/', '.');
    }

    /**
     * Convert a "."-based fully qualified class name to a "/"-based resource path.
     *
     * @param className the fully qualified class name
     * @return the corresponding resource path, pointing to the class
     */
    public static String convertClassNameToResourcePath(String className) {
        Assert.notNull(className, "Class name must not be null");
        return className.replace('.', '/');
    }

}
