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

package com.mycila.jmx.export;

import com.mycila.jmx.util.ClassUtils;
import com.mycila.jmx.util.ReflectionUtils;
import com.mycila.jmx.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class BeanProperty<T> {
    private final Method readMethod;
    private final Method writeMethod;
    private final Class<T> type;
    private final String name;

    private BeanProperty(String name, Method readMethod, Method writeMethod) {
        if (readMethod == null && writeMethod == null)
            throw new IllegalArgumentException("Invalid property " + name + ": missing at least one accessor method");
        if (readMethod != null && writeMethod != null && !readMethod.getReturnType().equals(writeMethod.getParameterTypes()[0]))
            throw new IllegalArgumentException("return type differs: " + readMethod.getReturnType() + " and " + writeMethod.getParameterTypes()[0]);
        this.name = name;
        this.type = (Class<T>) (readMethod != null ? readMethod.getReturnType() : writeMethod.getParameterTypes()[0]);
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public Class<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isReadable() {
        return readMethod != null;
    }

    public boolean isWritable() {
        return writeMethod != null;
    }

    public T get(Object o) throws Throwable {
        if (!isReadable())
            throw new IllegalStateException("Property not readable: " + this);
        if (!readMethod.isAccessible())
            readMethod.setAccessible(true);
        try {
            Object res = readMethod.invoke(o);
            if (!ClassUtils.isAssignableValue(getType(), res))
                throw new IllegalArgumentException("Invalid property: got type " + res.getClass().getName() + " but expect " + getType().getName());
            return (T) res;
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    public void set(Object o, T value) throws Throwable {
        if (!isWritable())
            throw new IllegalStateException("Property not writable: " + this);
        if (!writeMethod.isAccessible())
            writeMethod.setAccessible(true);
        try {
            writeMethod.invoke(o, value);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    @Override
    public String toString() {
        return ClassUtils.getQualifiedName(getType()) + " " + getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanProperty that = (BeanProperty) o;
        return name.equals(that.name) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public static BeanProperty<?> findProperty(Class<?> clazz, Method method) {
        if (ReflectionUtils.isIsMethod(method))
            return findProperty(clazz, StringUtils.uncapitalize(method.getName().substring(2)), method.getReturnType());
        if (ReflectionUtils.isGetMethod(method))
            return findProperty(clazz, StringUtils.uncapitalize(method.getName().substring(3)), method.getReturnType());
        if (ReflectionUtils.isSetter(method))
            return findProperty(clazz, StringUtils.uncapitalize(method.getName().substring(3)), method.getParameterTypes()[0]);
        return null;
    }

    public static BeanProperty<?> findProperty(Class<?> clazz, String property) {
        return findProperty(clazz, property, null);
    }

    public static <T> BeanProperty<T> findProperty(Class<?> clazz, String property, Class<T> type) {
        String name = StringUtils.capitalize(property);
        Method is = ReflectionUtils.findMethod(clazz, "is" + name, type);
        Method get = ReflectionUtils.findMethod(clazz, "get" + name, type);
        Method setter = ReflectionUtils.findMethod(clazz, "set" + name, Void.TYPE, type);
        Method getter = get != null ? get : is;
        if (setter == null && getter == null
                || setter != null && getter != null && !setter.getParameterTypes()[0].equals(getter.getReturnType()))
            return null;
        return new BeanProperty<T>(property, getter, setter);
    }

}
