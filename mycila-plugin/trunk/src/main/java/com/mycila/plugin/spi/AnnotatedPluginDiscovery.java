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

package com.mycila.plugin.spi;

import com.mycila.plugin.Loader;
import com.mycila.plugin.PluginDiscovery;
import com.mycila.plugin.annotation.Plugin;
import com.mycila.plugin.err.PluginDiscoveryException;
import com.mycila.plugin.spi.internal.ASMClassFinder;
import com.mycila.plugin.spi.internal.ClassUtils;
import com.mycila.plugin.spi.internal.ResourcePatternResolver;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotatedPluginDiscovery implements PluginDiscovery {

    public static final String[] DEFAULT_EXCLUDED_PACKAGES = {"java", "javax", "sun", "sunw"};
    public static final String[] DEFAULT_INCLUDED_PACKAGES = {};

    private final Class<? extends Annotation> annotationClass;
    private final Loader loader;

    private String[] includedPackages = DEFAULT_INCLUDED_PACKAGES;
    private String[] excludedPackages = DEFAULT_EXCLUDED_PACKAGES;
    
    public AnnotatedPluginDiscovery(Loader loader) {
        this(Plugin.class, loader);
    }

    AnnotatedPluginDiscovery(Class<? extends Annotation> annotationClass, Loader loader) {
        this.annotationClass = annotationClass;
        this.loader = loader;
    }

    public void includePackages(String... packages) {
        this.includedPackages = packages;
    }

    public void excludePackages(String... packages) {
        this.excludedPackages = packages;
    }

    @Override
    public Iterable<Class<?>> scan() throws PluginDiscoveryException {
        ExecutorService executorService = Executors.newFixedThreadPool((int) (Runtime.getRuntime().availableProcessors() * 1.5));
        ExecutorCompletionService<Class<?>> completionService = new ExecutorCompletionService<Class<?>>(executorService);
        try {
            long count = submitTasks(completionService);
            SortedSet<Class<?>> plugins = collectResults(completionService, count);
            return Collections.unmodifiableSortedSet(plugins);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new PluginDiscoveryException(e.getCause(), annotationClass, includedPackages);
        } catch (IOException e) {
            throw new PluginDiscoveryException(e, annotationClass, includedPackages);
        } finally {
            executorService.shutdownNow();
        }
        return Collections.emptySet();
    }

    private SortedSet<Class<?>> collectResults(ExecutorCompletionService<Class<?>> completionService, long count) throws InterruptedException, ExecutionException {
        SortedSet<Class<?>> plugins = new TreeSet<Class<?>>(new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        while (count-- > 0) {
            Class<?> c = completionService.take().get();
            if (c != null) plugins.add(c);
        }
        return plugins;
    }

    private long submitTasks(ExecutorCompletionService<Class<?>> completionService) throws IOException {
        final ASMClassFinder classfinder = new ASMClassFinder(annotationClass, loader);

        final ResourcePatternResolver pathResolver = new ResourcePatternResolver(loader);
        String[] prefixes = new String[excludedPackages.length];
        for (int i = 0; i < prefixes.length; i++)
            prefixes[i] = ClassUtils.convertClassNameToResourcePath(excludedPackages[i]) + "/";
        pathResolver.setExcludePrefixes(prefixes);

        long count = 0;
        if (includedPackages == null || includedPackages.length == 0) {
            for (final URL url : pathResolver.getResources("classpath*:**/*.class")) {
                count++;
                completionService.submit(new Callable<Class<?>>() {
                    @Override
                    public Class<?> call() throws Exception {
                        return classfinder.resolve(url);
                    }
                });
            }
        } else {
            for (String p : includedPackages) {
                for (final URL url : pathResolver.getResources("classpath*:" + ClassUtils.convertClassNameToResourcePath(p) + "/**/*.class")) {
                    count++;
                    completionService.submit(new Callable<Class<?>>() {
                        @Override
                        public Class<?> call() throws Exception {
                            return classfinder.resolve(url);
                        }
                    });
                }
            }
        }
        return count;
    }

}
