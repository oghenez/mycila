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

package com.mycila.plugin;

import com.mycila.plugin.err.PluginException;
import com.mycila.plugin.spi.Injector;
import com.mycila.plugin.spi.PluginMetadata;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginManager {

    private final Future<Injector> injector;

    public PluginManager(PluginDiscovery discovery) {
        this.injector = init(discovery);
    }

    public void start() {

    }

    public void stop() {

    }

    private Injector injector() {
        try {
            return injector.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PluginException(e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Error) throw (Error) cause;
            if (cause instanceof RuntimeException) throw (RuntimeException) cause;
            throw new PluginException(cause);
        }
    }

    private static Object instanciate(Class<?> pluginClass) {
        try {
            return pluginClass.getConstructor().newInstance();
        } catch (InvocationTargetException e) {
            throw new PluginException(e.getTargetException());
        } catch (Exception e) {
            throw new PluginException(e);
        }
    }

    private static Future<Injector> init(final PluginDiscovery discovery) {
        FutureTask<Injector> futureTask = new FutureTask<Injector>(new Callable<Injector>() {
            @Override
            public Injector call() throws Exception {
                List<PluginMetadata> metadatas = new LinkedList<PluginMetadata>();
                for (Class<?> pluginClass : discovery.scan())
                    metadatas.add(PluginMetadata.from(instanciate(pluginClass)));
                return Injector.build(metadatas);
            }
        });
        new Thread(futureTask, PluginManager.class.getName()).start();
        return futureTask;
    }

}
