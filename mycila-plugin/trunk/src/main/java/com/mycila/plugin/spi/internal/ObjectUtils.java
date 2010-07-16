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

public final class ObjectUtils {
    private ObjectUtils() {
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