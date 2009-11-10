package com.mycila.event.api;

import com.mycila.event.api.Ref;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum Reachability {
    HARD {
        @Override
        public <T> Ref<T> toRef(final T referencable) {
            return new Ref<T>() {
                @Override
                public T get() {
                    return referencable;
                }
            };
        }},

    WEAK {
        @Override
        public <T> Ref<T> toRef(T referencable) {
            return new JDKRef<T>(new WeakReference<T>(referencable));
        }},

    SOFT {
        @Override
        public <T> Ref<T> toRef(T referencable) {
            return new JDKRef<T>(new SoftReference<T>(referencable));
        }};

    public abstract <T> Ref<T> toRef(T referencable);

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
