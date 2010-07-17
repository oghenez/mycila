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

import com.google.common.collect.Iterables;
import com.mycila.plugin.spi.internal.ClassUtils;
import com.mycila.plugin.test.ShowDuration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.annotation.Retention;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class AnnotatedPluginDiscoveryPerfTest {

    static {
        ConsoleHandler handler = new ConsoleHandler() {
            {
                setOutputStream(System.out);
            }
        };
        handler.setLevel(Level.ALL);
        Logger.getLogger("com.mycila").addHandler(handler);
        Logger.getLogger("com.mycila").setLevel(Level.ALL);
    }

    @Rule
    public ShowDuration showDurationRule = new ShowDuration();

    @Test
    public void test_local() throws Exception {
        AnnotatedPluginDiscovery discovery = new AnnotatedPluginDiscovery(Retention.class, new DefaultLoader(ClassUtils.getDefaultClassLoader()));
        discovery.includePackages("com.mycila.plugin.annotation");
        Iterable<Class<?>> it = discovery.scan();
        System.out.println(Iterables.toString(it));
        assertEquals(15, Iterables.size(it));
    }

    @Test
    public void test_large() throws Exception {
        AnnotatedPluginDiscovery discovery = new AnnotatedPluginDiscovery(Retention.class, new DefaultLoader(ClassUtils.getDefaultClassLoader()));
        for (Class<?> aClass : discovery.scan()) ;
        //System.out.println(aClass);
    }

    @Test
    public void test_large_exclude() throws Exception {
        AnnotatedPluginDiscovery discovery = new AnnotatedPluginDiscovery(Retention.class, new DefaultLoader(ClassUtils.getDefaultClassLoader()));
        discovery.excludePackages("com.sun", "com.google");
        for (Class<?> aClass : discovery.scan()) ;
        //System.out.println(aClass);
    }

}
