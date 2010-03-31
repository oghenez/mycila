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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class BeanProperty<T> {
    private Method readMethod;
    private Method writeMethod;
    private Class<T> type;
    private String name;

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
            return getType().cast(readMethod.invoke(o));
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
        return getName();
    }

}
