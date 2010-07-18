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

import com.mycila.plugin.PluginDiscovery;
import com.mycila.plugin.PluginManager;
import com.mycila.plugin.err.InvokeException;
import com.mycila.plugin.err.PluginException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentPluginManager implements PluginManager {

    private final Future<PluginManager> pluginManager;

    public ConcurrentPluginManager(final PluginDiscovery discovery) {
        FutureTask<PluginManager> delegate = new FutureTask<PluginManager>(new Callable<PluginManager>() {
            @Override
            public PluginManager call() throws Exception {
                return DefaultPluginManager.build(discovery.scan());
            }
        });
        new Thread(delegate, ConcurrentPluginManager.class.getName()).start();
        this.pluginManager = delegate;
    }

    @Override
    public void start() throws InvokeException {
        delegate().start();
    }

    @Override
    public void stop() throws InvokeException {
        delegate().stop();
    }

    private PluginManager delegate() {
        try {
            return pluginManager.get();
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
}
