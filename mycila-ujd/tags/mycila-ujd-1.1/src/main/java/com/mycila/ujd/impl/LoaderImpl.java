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
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.JavaClass;
import com.mycila.ujd.api.Loader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class LoaderImpl implements Loader {

    private final ClassLoader classLoader;
    private final DefaultJVM jvm;

    LoaderImpl(DefaultJVM jvm, ClassLoader classLoader) {
        this.jvm = jvm;
        this.classLoader = classLoader;
    }

    public Iterable<? extends Loader> getChilds() {
        return Iterables.filter(jvm.getLoaders(), new Predicate<Loader>() {
            public boolean apply(Loader input) {
                return input.getParent() == LoaderImpl.this;
            }
        });
    }

    public String getName() {
        return classLoader.getClass().getName() + "@" + Integer.toHexString(classLoader.hashCode());
    }

    public Loader getParent() {
        return jvm.loaderRegistry.get(get().getParent());
    }

    public Iterable<? extends Container> getContainers() {
        final String javaHome = System.getProperty("java.home");
        return classLoader instanceof URLClassLoader ?
                Iterables.transform(Iterables.filter(Arrays.asList(((URLClassLoader) classLoader).getURLs()), new Predicate<URL>() {
                    public boolean apply(URL url) {
                        return !url.toExternalForm().contains(javaHome);
                    }
                }), new Function<URL, Container>() {
                    public Container apply(URL from) {
                        return jvm.containerRegistry.get(from);
                    }
                }) : Collections.<Container>emptyList();
    }

    public Iterable<? extends JavaClass<?>> getClasses() {
        return Iterables.filter(jvm.getClasses(), new Predicate<JavaClass<?>>() {
            public boolean apply(JavaClass<?> input) {
                return input.getLoader() == LoaderImpl.this;
            }
        });
    }

    public ClassLoader get() {
        return classLoader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoaderImpl loader = (LoaderImpl) o;
        return classLoader.equals(loader.classLoader);
    }

    @Override
    public int hashCode() {
        return classLoader.hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }
}
