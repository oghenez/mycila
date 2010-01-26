package com.mycila.ujd.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.mycila.ujd.api.ContainedClass;
import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.JavaClass;
import com.mycila.ujd.api.Loader;

import java.util.Iterator;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JVMImpl implements JVM {

    final JavaClassRegistry classRegistry = new JavaClassRegistry(this);
    final LoaderRegistry loaderRegistry = new LoaderRegistry(this);
    final ContainerRegistry containerRegistry = new ContainerRegistry();

    public Iterable<? extends JavaClass<?>> getClasses() {
        return classRegistry.getJavaClasses();
    }

    public <T extends JavaClass<?>> Iterable<T> getClasses(Predicate<? super T> predicate) {
        return Iterables.<T>filter((Iterable<T>) getClasses(), predicate);
    }

    public Iterable<? extends Loader> getLoaders() {
        return new Iterable<Loader>() {
            public Iterator<Loader> iterator() {
                return new MemoizingIterator<Loader>(Iterators.transform(
                        getClasses().iterator(),
                        new Function<JavaClass<?>, Loader>() {
                            public Loader apply(JavaClass<?> from) {
                                return from.getLoader();
                            }
                        }));
            }
        };
    }

    public Iterable<? extends Container> getContainers() {
        return new Iterable<Container>() {
            public Iterator<Container> iterator() {
                return new MemoizingIterator<Container>(Iterables.concat(Iterables.transform(
                        getLoaders(),
                        new Function<Loader, Iterable<? extends Container>>() {
                            public Iterable<? extends Container> apply(Loader from) {
                                return from.getContainers();
                            }
                        })).iterator());
            }
        };
    }

    public Iterable<? extends ContainedClass> getContainedClasses() {
        return new Iterable<ContainedClass>() {
            public Iterator<ContainedClass> iterator() {
                return new MemoizingIterator<ContainedClass>(Iterables.concat(Iterables.transform(
                        getContainers(),
                        new Function<Container, Iterable<? extends ContainedClass>>() {
                            public Iterable<? extends ContainedClass> apply(Container from) {
                                return from.getClasses();
                            }
                        })).iterator());
            }
        };
    }

}
