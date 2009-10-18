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
package com.mycila.testing.core.api;

import java.lang.reflect.AccessibleObject;

/**
 * Utility class
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Ensure {
    private Ensure() {
    }

    /**
     * Check for not null parameters and throw and exception is null
     *
     * @param argName Argument name
     * @param arg     Argument
     * @throws IllegalArgumentException If the arg is null
     */
    public static void notNull(String argName, Object arg) throws IllegalArgumentException {
        if (arg == null) {
            throw new IllegalArgumentException(argName + " cannot be null");
        }
    }

    /**
     * Calls {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)}
     * and returns the same object
     *
     * @param a   Object
     * @param <T> AccessibleObject instance
     * @return the same object beging 'accessible'
     */
    public static <T extends AccessibleObject> T accessible(T a) {
        if (!a.isAccessible()) {
            a.setAccessible(true);
        }
        return a;
    }

}
