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

package com.mycila.plugin.old;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Ensure {
    private Ensure() {
    }

    static void notNull(String argName, Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException(argName + " cannot be null");
        }
    }

    static void notEmpty(String message, String argName, String arg) {
        notNull(argName, arg);
        if (isEmpty(arg)) {
            throw new IllegalArgumentException(argName + " cannot be empty: " + message);
        }
    }

    static void notEmpty(String argName, String arg) {
        notNull(argName, arg);
        for (int i = 0; i < arg.length(); i++) {
            if (!Character.isWhitespace(arg.charAt(i))) {
                return;
            }
        }
        throw new IllegalArgumentException(argName + " cannot be empty");
    }

    static boolean isEmpty(String arg) {
        for (int i = 0; i < arg.length(); i++) {
            if (!Character.isWhitespace(arg.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}