/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.event;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Providers {

    private Providers() {
    }

    public static <T> Provider<T> cache(final Provider<? extends T> p) {
        return new Provider<T>() {
            private final T t = p.get();

            @Override
            public T get() {
                return t;
            }
        };
    }

    public static <T> Provider<T> cache(final T val) {
        return new Provider<T>() {
            private final T t = val;

            @Override
            public T get() {
                return t;
            }
        };
    }

}
