/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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
package com.mycila.testing.plugin.db;

import java.util.HashMap;
import java.util.Map;

final class ClassUtils {

    private ClassUtils() {
    }

    /**
     * Map with primitive wrapper type as key and corresponding primitive
     * type as value, for example: Integer.class -> int.class.
     */
    private static final Map<Class, Class> wrapperPrimitives = new HashMap<Class, Class>(8);
    private static final Map<Class, Class> primitiveWrappers = new HashMap<Class, Class>(8);

    static {
        wrapperPrimitives.put(Boolean.class, boolean.class);
        wrapperPrimitives.put(Byte.class, byte.class);
        wrapperPrimitives.put(Character.class, char.class);
        wrapperPrimitives.put(Double.class, double.class);
        wrapperPrimitives.put(Float.class, float.class);
        wrapperPrimitives.put(Integer.class, int.class);
        wrapperPrimitives.put(Long.class, long.class);
        wrapperPrimitives.put(Short.class, short.class);
        for (Map.Entry<Class, Class> entry : wrapperPrimitives.entrySet()) {
            primitiveWrappers.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * Check if the given class represents a primitive wrapper,
     * i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
     *
     * @param clazz the class to check
     * @return whether the given class is a primitive wrapper class
     */
    public static boolean isPrimitiveWrapper(Class clazz) {
        return wrapperPrimitives.containsKey(clazz);
    }

    /**
     * Check if the given class represents a primitive (i.e. boolean, byte,
     * char, short, int, long, float, or double) or a primitive wrapper
     * (i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double).
     *
     * @param clazz the class to check
     * @return whether the given class is a primitive or primitive wrapper class
     */
    public static boolean isPrimitiveOrWrapper(Class clazz) {
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    /**
     * Check if the given class represents an array of primitives,
     * i.e. boolean, byte, char, short, int, long, float, or double.
     *
     * @param clazz the class to check
     * @return whether the given class is a primitive array class
     */
    public static boolean isPrimitiveArray(Class clazz) {
        return (clazz.isArray() && clazz.getComponentType().isPrimitive());
    }

    /**
     * Check if the given class represents an array of primitive wrappers,
     * i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
     *
     * @param clazz the class to check
     * @return whether the given class is a primitive wrapper array class
     */
    public static boolean isPrimitiveWrapperArray(Class clazz) {
        return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
    }

    /**
     * Check if the right-hand side type may be assigned to the left-hand side
     * type, assuming setting by reflection. Considers primitive wrapper
     * classes as assignable to the corresponding primitive types.
     *
     * @param lhsType the target type
     * @param rhsType the value type that should be assigned to the target type
     * @return if the target type is assignable from the value type
     * @see org.springframework.util.TypeUtils#isAssignable
     */
    public static boolean isAssignable(Class lhsType, Class rhsType) {
        return (lhsType.isAssignableFrom(rhsType) ||
                lhsType.equals(wrapperPrimitives.get(rhsType)));
    }

    /**
     * Determine if the given type is assignable from the given value,
     * assuming setting by reflection. Considers primitive wrapper classes
     * as assignable to the corresponding primitive types.
     *
     * @param type  the target type
     * @param value the value that should be assigned to the type
     * @return if the type is assignable from the value
     */
    public static boolean isAssignableValue(Class type, Object value) {
        return (value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive());
    }

    public static boolean isNumber(Class type) {
        return Number.class.isAssignableFrom(type) || type.isPrimitive() && Number.class.isAssignableFrom(primitiveWrappers.get(type));
    }

    public static boolean isNumber(Object object) {
        return Number.class.isAssignableFrom(object.getClass());
    }

}
