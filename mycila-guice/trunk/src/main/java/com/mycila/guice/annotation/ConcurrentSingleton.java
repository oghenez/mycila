/**
 * Copyright (C) 2010 mycila.com <mathieu.carbou@gmail.com>
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

package com.mycila.guice.annotation;

import com.google.inject.BindingAnnotation;
import com.google.inject.ScopeAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;

/**
 * Apply this to implementation classes when you want only one instance
 * (per {@link com.google.inject.Injector}) to be reused for all injections for that binding,
 * and you want to eagerly create the instance concurrently in the background
 * with other singletons in this scope.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RUNTIME)
@ScopeAnnotation
public @interface ConcurrentSingleton {

    @Retention(RUNTIME)
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
    @Inherited
    @BindingAnnotation
    static @interface Timeout {
    }

}