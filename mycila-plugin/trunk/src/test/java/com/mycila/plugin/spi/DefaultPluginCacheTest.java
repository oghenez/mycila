/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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

import com.mycila.plugin.api.PluginCache;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultPluginCacheTest {

    PluginCache<MyPlugin> cache;

    @BeforeMethod
    public void resetAndGet() {
        cache = new DefaultPluginCache<MyPlugin>(new DefaultPluginLoader<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/spi/two.properties"));
        assertEquals(cache.getBindings().size(), 2);
    }

    @Test
    public void test_setPlugins() {
        cache.registerPlugins(new HashMap<String, MyPlugin>() {
            {
                put("AAA", new MyPlugin1());
                put("BBB", new MyPlugin1());
            }
        });
        assertEquals(cache.getBindings().size(), 4);
    }

    @Test
    public void test_setPlugin() {
        cache.registerPlugin("aaaa", new MyPlugin1());
        cache.registerPlugin("aaaa", new MyPlugin1());
        cache.registerPlugin("aaaa", new MyPlugin1());
        assertEquals(cache.getBindings().size(), 3);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_setPlugin_invalid() {
        cache.registerPlugin(null, new MyPlugin1());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_setPlugin_invalid1() {
        cache.registerPlugin("", new MyPlugin1());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_setPlugin_invalid2() {
        cache.registerPlugin("", new MyPlugin1());
    }

    @Test
    public void test_clear() {
        cache.registerPlugin("aaaa", new MyPlugin1());
        assertEquals(cache.getBindings().size(), 3);
        cache.clear();
        assertEquals(cache.getBindings().size(), 2);
    }

    @Test
    public void test_remove() {
        cache.removePlugins("plugin1", "plugin2");
        assertEquals(cache.getBindings().size(), 0);
    }

    @Test
    public void test_concurrency() throws Exception {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(20);

        for (int i = 0; i < 100; ++i)
            new Thread(new Worker(startSignal, doneSignal) {
                void execute() {
                    cache.clear();
                }
            }, "Worker-Get").start();

        for (int i = 0; i < 100; ++i)
            new Thread(new Worker(startSignal, doneSignal) {
                void execute() {
                    cache.getBindings();
                }
            }, "Worker-Clear").start();

        startSignal.countDown();
        doneSignal.await();
    }

    static abstract class Worker implements Runnable {
        CountDownLatch startSignal;
        CountDownLatch doneSignal;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        public void run() {
            try {
                startSignal.await();
                execute();
                doneSignal.countDown();
            } catch (InterruptedException ex) {
            }
        }

        abstract void execute();
    }
}
