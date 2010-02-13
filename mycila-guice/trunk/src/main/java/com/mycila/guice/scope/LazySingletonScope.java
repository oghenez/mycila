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

package com.mycila.guice.scope;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.mycila.guice.annotation.LazySingleton;
import org.guiceyfruit.support.HasScopeAnnotation;

import java.lang.annotation.Annotation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class LazySingletonScope implements Scope, HasScopeAnnotation {

    @Override
    public Class<? extends Annotation> getScopeAnnotation() {
        return LazySingleton.class;
    }

    public <T> Provider<T> scope(Key<T> key, final Provider<T> creator) {
        return new Provider<T>() {

            private volatile T instance;

            // DCL on a volatile is safe as of Java 5, which we obviously require.
            @SuppressWarnings("DoubleCheckedLocking")
            public T get() {
                if (instance == null) {
                    /*
                    * Use a pretty coarse lock. We don't want to run into deadlocks
                    * when two threads try to load circularly-dependent objects.
                    * Maybe one of these days we will identify independent graphs of
                    * objects and offer to load them in parallel.
                    */
                    synchronized (LazySingletonScope.class) {
                        if (instance == null) {
                            instance = creator.get();
                        }
                    }
                }
                return instance;
            }

            public String toString() {
                return String.format("%s[%s]", creator, LazySingletonScope.this);
            }
        };
    }

    @Override
    public String toString() {
        return "LazySingletonScope";
    }
}