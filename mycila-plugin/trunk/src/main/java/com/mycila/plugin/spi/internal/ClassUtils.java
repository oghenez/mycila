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

package com.mycila.plugin.spi.internal;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ClassUtils {

    private ClassUtils() {
    }

    private static final boolean hasCGLIB;
    private static final boolean hasASM;
    private static final boolean hasAOP;
    private static final boolean hasAnnotationType;

    static {
        hasCGLIB = hasCGLIB(ClassUtils.getDefaultClassLoader());
        hasASM = hasASM(ClassUtils.getDefaultClassLoader());
        hasAOP = hasAOP(ClassUtils.getDefaultClassLoader());
        hasAnnotationType = hasAnnotationType(ClassUtils.getDefaultClassLoader());
    }

    public static boolean hasCGLIB() {
        return hasCGLIB;
    }

    public static boolean hasCGLIB(ClassLoader classLoader) {
        try {
            classLoader.loadClass("net.sf.cglib.proxy.Callback");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static boolean hasASM() {
        return hasASM;
    }

    public static boolean hasASM(ClassLoader classLoader) {
        try {
            classLoader.loadClass("org.objectweb.asm.Type");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static boolean hasAOP() {
        return hasAOP;
    }

    public static boolean hasAOP(ClassLoader classLoader) {
        try {
            classLoader.loadClass("org.aopalliance.aop.Advice");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static boolean hasAnnotationType() {
        return hasAOP;
    }

    public static boolean hasAnnotationType(ClassLoader classLoader) {
        try {
            classLoader.loadClass("sun.reflect.annotation.AnnotationType");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
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

    public static String[] toNames(Class<?>[] classes) {
        String[] names = new String[classes.length];
        for (int i = 0; i < names.length; i++)
            names[i] = classes[i].getName();
        return names;
    }

    public static <T> T instanciate(Class<T> pluginClass) throws Throwable {
        try {
            return pluginClass.getConstructor().newInstance();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

}
