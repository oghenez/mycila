package com.mycila.ujd.impl;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.mycila.ujd.api.ContainedClass;
import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.LoadedClass;
import com.mycila.ujd.api.Loader;

import java.util.Iterator;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JVMImpl implements JVM {

    final JavaClassManager classManager = new JavaClassManager();

    public Iterable<? extends LoadedClass> getLoadedClasses() {
        return Iterables.filter(classManager.getJavaClasses(), LoadedClass.class);
    }

    public Iterable<? extends Loader> getLoaders() {
        return new Iterable<Loader>() {
            public Iterator<Loader> iterator() {
                return new MemoizingIterator<Loader>(Iterators.transform(
                        getLoadedClasses().iterator(),
                        new Function<LoadedClass, Loader>() {
                            public Loader apply(LoadedClass from) {
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
