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
import com.mycila.plugin.PluginDiscoveryException;
import com.mycila.plugin.annotation.Plugin;
import com.mycila.plugin.spi.internal.ASMClassResolver;
import com.mycila.plugin.spi.internal.ClassResolver;
import com.mycila.plugin.spi.internal.ResourcePatternResolver;
import com.mycila.plugin.spi.internal.util.ClassUtils;
import com.mycila.plugin.spi.internal.util.StringUtils;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotatedPluginDiscovery implements PluginDiscovery {

    private static final Logger LOGGER = Logger.getLogger(AnnotatedPluginDiscovery.class.getName());

    private final Class<? extends Annotation> annotationClass;
    private final ResourcePatternResolver pathResolver;

    private ClassResolver classResolver;
    private String[] includedPackages = new String[0];
    private String[] excludedPackages = {"java", "javax"};

    public AnnotatedPluginDiscovery(Loader loader) {
        this(Plugin.class, loader);
    }

    public AnnotatedPluginDiscovery(Class<? extends Annotation> annotationClass, Loader loader) {
        this.annotationClass = annotationClass;
        this.pathResolver = new ResourcePatternResolver(loader);
        this.classResolver = new ASMClassResolver(annotationClass, loader);
    }

    public void setClassResolver(ClassResolver classResolver) {
        this.classResolver = classResolver;
    }

    public void includePackages(String... packages) {
        this.includedPackages = packages;
    }

    public void excludePackages(String... packages) {
        this.excludedPackages = packages;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    @Override
    public Iterable<Class<?>> scan() throws PluginDiscoveryException {
        setExclusions();
        ExecutorService executorService = Executors.newFixedThreadPool((int) (Runtime.getRuntime().availableProcessors() * 1.5));
        ExecutorCompletionService<Class<?>> completionService = new ExecutorCompletionService<Class<?>>(executorService);
        try {
            long count = submitTasks(completionService);
            SortedSet<Class<?>> plugins = collectResults(completionService, count);
            if (LOGGER.isLoggable(Level.CONFIG))
                LOGGER.config("Found " + plugins.size() + " plugins.");
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
        long count = 0;
        if (includedPackages == null || includedPackages.length == 0) {
            if (LOGGER.isLoggable(Level.CONFIG))
                LOGGER.config("Scanning for classes annotated by @" + annotationClass.getSimpleName() + " in all packages, excluding packages " + StringUtils.arrayToCommaDelimitedString(excludedPackages) + "...");
            for (final URL url : pathResolver.getResources("classpath*:**/*.class")) {
                count++;
                completionService.submit(new Callable<Class<?>>() {
                    @Override
                    public Class<?> call() throws Exception {
                        return classResolver.resolve(url);
                    }
                });
            }
        } else {
            if (LOGGER.isLoggable(Level.CONFIG))
                LOGGER.config("Scanning for classes annotated by @" + annotationClass.getSimpleName() + " in packages " + StringUtils.arrayToCommaDelimitedString(includedPackages) + " excluding packages " + StringUtils.arrayToCommaDelimitedString(excludedPackages) + "...");
            for (String p : includedPackages) {
                for (final URL url : pathResolver.getResources("classpath*:" + ClassUtils.convertClassNameToResourcePath(p) + "/**/*.class")) {
                    count++;
                    completionService.submit(new Callable<Class<?>>() {
                        @Override
                        public Class<?> call() throws Exception {
                            return classResolver.resolve(url);
                        }
                    });
                }
            }
        }
        return count;
    }

    private void setExclusions() {
        String[] prefixes = new String[excludedPackages.length];
        for (int i = 0; i < prefixes.length; i++)
            prefixes[i] = ClassUtils.convertClassNameToResourcePath(excludedPackages[i]) + "/";
        this.pathResolver.setExcludePrefixes(prefixes);
    }

}
