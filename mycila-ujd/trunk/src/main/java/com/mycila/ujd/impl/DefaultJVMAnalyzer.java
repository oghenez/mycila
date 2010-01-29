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

package com.mycila.ujd.impl;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.mycila.ujd.api.ContainedClass;
import com.mycila.ujd.api.ContainedJavaClass;
import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.JVMAnalyzer;
import com.mycila.ujd.api.JavaClass;
import com.mycila.ujd.api.Loader;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultJVMAnalyzer implements JVMAnalyzer {

    private final JVM jvm;

    public DefaultJVMAnalyzer(JVM jvm) {
        this.jvm = jvm;
    }

    public JVM getJVM() {
        return jvm;
    }

    public int getClassCount() {
        return Iterables.size(jvm.getClasses());
    }

    public int getLoaderCount() {
        return Iterables.size(jvm.getLoaders());
    }

    public Iterable<String> getLoaderNames() {
        SortedSet<String> names = new TreeSet<String>();
        for (Loader loader : jvm.getLoaders()) names.add(loader.getName());
        return names;
    }

    public Iterable<? extends Container> getClassPath(final String loaderName) {
        if (loaderName == null) throw new IllegalArgumentException("Loader name parameter is required");
        try {
            return Iterables.find(jvm.getLoaders(), new Predicate<Loader>() {
                public boolean apply(Loader input) {
                    return input.getName().equals(loaderName);
                }
            }).getContainers();
        } catch (NoSuchElementException e) {
            return Collections.<Container>emptySet();
        }
    }

    public Iterable<? extends ContainedClass> getClasses(String loaderName, final String packagePrefix) {
        return Iterables.filter(Iterables.concat(Iterables.transform(getClassPath(loaderName), new Function<Container, Iterable<? extends ContainedClass>>() {
            public Iterable<? extends ContainedClass> apply(Container from) {
                return from.getClasses();
            }
        })), new Predicate<ContainedClass>() {
            public boolean apply(ContainedClass input) {
                return packagePrefix == null || input.getClassName().startsWith(packagePrefix);
            }
        });
    }

    public Iterable<? extends ContainedJavaClass<?>> getUsedClasses(final String loaderName, final String packagePrefix) {
        if (loaderName == null) throw new IllegalArgumentException("Loader name parameter is required");
        return jvm.getClasses(new Predicate<JavaClass<?>>() {
            public boolean apply(JavaClass<?> input) {
                return input instanceof ContainedJavaClass
                        && input.getLoader().getName().equals(loaderName)
                        && (packagePrefix == null || input.getClass().getName().startsWith(packagePrefix));
            }
        });
    }

    public Iterable<? extends Container> getUsedClassPath(final String loaderName) {
        if (loaderName == null) throw new IllegalArgumentException("Loader name parameter is required");
        return new Iterable<Container>() {
            public Iterator<Container> iterator() {
                return new MemoizingIterator<Container>(Iterables.transform(getUsedClasses(loaderName, null), new Function<ContainedJavaClass<?>, Container>() {
                    public Container apply(ContainedJavaClass<?> from) {
                        return from.getContainer();
                    }
                }).iterator());
            }
        };
    }

    public Iterable<? extends ContainedClass> getUnusedClasses(String loaderName, String packagePrefix) {
        final Set<String> used = new HashSet<String>();
        Iterables.addAll(used, Iterables.transform(getUsedClasses(loaderName, packagePrefix), Functions.toStringFunction()));
        return Iterables.filter(getClasses(loaderName, packagePrefix), new Predicate<ContainedClass>() {
            public boolean apply(ContainedClass input) {
                return !used.contains(input.getClassName());
            }
        });
    }

    public Iterable<? extends Container> getUnusedClassPath(final String loaderName) {
        final Set<String> used = new HashSet<String>();
        Iterables.addAll(used, Iterables.transform(getUsedClassPath(loaderName), Functions.toStringFunction()));
        return Iterables.filter(getClassPath(loaderName), new Predicate<Container>() {
            public boolean apply(Container input) {
                return !used.contains(input.toString());
            }
        });
    }

}
