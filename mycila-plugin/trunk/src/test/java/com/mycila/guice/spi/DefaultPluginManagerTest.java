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

package com.mycila.guice.spi;

import com.mycila.guice.CyclicPluginDependencyException;
import com.mycila.guice.PluginManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class DefaultPluginManagerTest {

    @Test
    public void test_circual() throws Exception {
        try {
            DefaultPluginManager.build(new CustomPluginDiscovery()
                    .add(PluginA.class)
                    .add(PluginB.class)
                    .add(PluginC.class)
                    .scan());
            fail();
        } catch (Exception e) {
            assertEquals(CyclicPluginDependencyException.class, e.getClass());
        }
    }

    @Test
    public void test_order() throws Exception {
        PluginManager manager = DefaultPluginManager.build(new CustomPluginDiscovery()
                .add(PluginA.class)
                .add(PluginB.class)
                .add(PluginD.class)
                .scan());
        Collector.clear();
        manager.activate();
        assertArrayEquals(new String[]{"B", "A", "D"}, Collector.get());
    }

    @Test
    public void test_bad_methods() throws Exception {
        try {
            DefaultPluginManager.build(new CustomPluginDiscovery()
                    .add(PluginC.class)
                    .scan());
            fail();
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
        try {
            DefaultPluginManager.build(new CustomPluginDiscovery()
                    .add(PluginE.class)
                    .scan());
            fail();
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

}
