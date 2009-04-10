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
package com.mycila.log.jdk.format;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Utils {
    private Utils() {
    }

    @SuppressWarnings({"unchecked"})
    public static final String EOL = java.security.AccessController.<String>doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

    @SuppressWarnings({"unchecked"})
    public static <T> T instantiateByClassName(String className, Class superClass, T defaultValue) {
        if (className != null) {
            try {
                Class classObj = Thread.currentThread().getContextClassLoader().loadClass(className);
                if (!superClass.isAssignableFrom(classObj)) {
                    return defaultValue;
                }
                return (T) classObj.newInstance();
            } catch (Exception ignored) {
            }
        }
        return defaultValue;
    }
}