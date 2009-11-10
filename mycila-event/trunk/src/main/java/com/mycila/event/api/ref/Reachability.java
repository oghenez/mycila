package com.mycila.event.api.ref;

import com.mycila.event.api.Ensure;
import static com.mycila.event.api.Ensure.*;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

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
