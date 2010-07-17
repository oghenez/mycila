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

import java.util.HashMap;
import java.util.Map;

final class ObjectUtils {
    private ObjectUtils() {
    }

    private static final Map<Class<?>, Object> DEFAULTS = new HashMap<Class<?>, Object>(16) {
        {
            put(boolean.class, false);
            put(char.class, '\0');
            put(byte.class, (byte) 0);
            put(short.class, (short) 0);
            put(int.class, 0);
            put(long.class, 0L);
            put(float.class, 0f);
            put(double.class, 0d);
        }
    };

    /**
     * Returns the default value of {@code type} as defined by JLS --- {@code 0}
     * for numbers, {@code false} for {@code boolean} and {@code '\0'} for {@code
     * char}. For non-primitive types and {@code void}, null is returned.
     */
    @SuppressWarnings("unchecked")
    public static <T> T defaultValue(Class<T> type) {
        return (T) DEFAULTS.get(type);
    }

    /*
     * This method, which clones its array argument, would not be necessary
     * if Cloneable had a public clone method.
     */
    public static Object cloneArray(Object array) {
        Class type = array.getClass();
        if (type == byte[].class) {
            byte[] byteArray = (byte[]) array;
            return byteArray.clone();
        }
        if (type == char[].class) {
            char[] charArray = (char[]) array;
            return charArray.clone();
        }
        if (type == double[].class) {
            double[] doubleArray = (double[]) array;
            return doubleArray.clone();
        }
        if (type == float[].class) {
            float[] floatArray = (float[]) array;
            return floatArray.clone();
        }
        if (type == int[].class) {
            int[] intArray = (int[]) array;
            return intArray.clone();
        }
        if (type == long[].class) {
            long[] longArray = (long[]) array;
            return longArray.clone();
        }
        if (type == short[].class) {
            short[] shortArray = (short[]) array;
            return shortArray.clone();
        }
        if (type == boolean[].class) {
            boolean[] booleanArray = (boolean[]) array;
            return booleanArray.clone();
        }
        Object[] objectArray = (Object[]) array;
        return objectArray.clone();
    }

}