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

package com.mycila.event.api;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum Reachability {
    HARD {
        @Override
        public <T> Ref<T> wrap(final T referencable) {
            notNull(referencable, "Referenced object");
            return new Ref<T>() {
                @Override
                public T get() {
                    return referencable;
                }
            };
        }},

    WEAK {
        @Override
        public <T> Ref<T> wrap(T referencable) {
            return new JDKRef<T>(new WeakReference<T>(notNull(referencable, "Referenced object")));
        }},

    SOFT {
        @Override
        public <T> Ref<T> wrap(T referencable) {
            return new JDKRef<T>(new SoftReference<T>(notNull(referencable, "Referenced object")));
        }};

    public abstract <T> Ref<T> wrap(T referencable);

    public static Reachability of(Object o) {
        notNull(o, "Object");
        return of(o.getClass());
    }

    public static Reachability of(Class<?> c) {
        notNull(c, "Class");
        com.mycila.event.api.annotation.Reference ref = c.getAnnotation(com.mycila.event.api.annotation.Reference.class);
        return ref == null ? HARD : ref.value();
    }

    public static <T> Ref<T> reference(T o) {
        return of(o).wrap(o);
    }

    private static final class JDKRef<T> implements Ref<T> {
        final Reference<T> reference;

        JDKRef(Reference<T> reference) {
            this.reference = reference;
        }

        @Override
        public T get() {
            return reference.get();
        }
    }
}
