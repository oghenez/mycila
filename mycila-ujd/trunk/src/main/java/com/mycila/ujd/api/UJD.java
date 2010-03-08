package com.mycila.ujd.api;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.util.Collection;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class UJD {

    private UJD() {
    }

    public static final Function<JavaClass<?>, Loader> JAVACLASS_TO_LOADER
            = new Function<JavaClass<?>, Loader>() {
        public Loader apply(JavaClass<?> from) {
            return from.getLoader();
        }
    };

    public static final Function<Loader, Iterable<? extends Container>> LOADER_TO_CONTAINER
            = new Function<Loader, Iterable<? extends Container>>() {
        public Iterable<? extends Container> apply(Loader from) {
            return from.getContainers();
        }
    };

    public static final Function<Container, Iterable<? extends ContainedClass>> CONTAINER_TO_CONTAINED_CLASSES
            = new Function<Container, Iterable<? extends ContainedClass>>() {
        public Iterable<? extends ContainedClass> apply(Container from) {
            return from.getClasses();
        }
    };

    public static final Function<? extends ContainedClass, Container> CONTAINED_CLASS_TO_CONTAINER
            = new Function<ContainedJavaClass<?>, Container>() {
        public Container apply(ContainedJavaClass<?> from) {
            return from.getContainer();
        }
    };

    public static final Function<ContainedJavaClass<?>, String> CONTAINED_CLASS_NAME
            = new Function<ContainedJavaClass<?>, String>() {
        public String apply(ContainedJavaClass<?> from) {
            return from.getClassName();
        }
    };

    public static final Function<Loader, String> LOADER_NAME = new Function<Loader, String>() {
        public String apply(Loader loader) {
            return loader.getName();
        }
    };

    public static Predicate<Loader> hasParentLoader(final Loader loader) {
        return new Predicate<Loader>() {
            public boolean apply(Loader input) {
                return input.getParent() == loader;
            }
        };
    }

    public static Predicate<JavaClass<?>> hasLoader(final Loader loader) {
        return new Predicate<JavaClass<?>>() {
            public boolean apply(JavaClass<?> input) {
                return input.getLoader() == loader;
            }
        };
    }

    public static Predicate<JavaClass<?>> javaClassStartsWith(final String prefix) {
        return new Predicate<JavaClass<?>>() {
            public boolean apply(JavaClass<?> javaClass) {
                return javaClass.get().getName().startsWith(prefix);
            }
        };
    }

    public static Predicate<ContainedClass> containedClassStartsWith(final String prefix) {
        return new Predicate<ContainedClass>() {
            public boolean apply(ContainedClass containedClass) {
                return containedClass.getClassName().startsWith(prefix);
            }
        };
    }

    public static Predicate<ContainedClass> containedClassNameIn(final Collection<String> col) {
        return new Predicate<ContainedClass>() {
            public boolean apply(ContainedClass containedClass) {
                return col.contains(containedClass.getClassName());
            }
        };
    }

    public static Predicate<Loader> isLoaderNamed(final String loaderName) {
        return new Predicate<Loader>() {
            public boolean apply(Loader loader) {
                return loader.getName().equals(loaderName);
            }
        };
    }

    public static <T> Iterable<T> memoize(Iterable<? extends T> it) {
        return new MemoizingIterable<T>(it);
    }

}
