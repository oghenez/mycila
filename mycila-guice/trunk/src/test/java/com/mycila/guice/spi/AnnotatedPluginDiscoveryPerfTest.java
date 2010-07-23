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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.annotation.Retention;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class AnnotatedPluginDiscoveryPerfTest {

    @Test
    public void test_local() throws Exception {
        AnnotatedPluginDiscovery discovery = new AnnotatedPluginDiscovery(Retention.class, new DefaultLoader());
        discovery.includePackages("com.mycila.guice.annotation");
        int count = 0;
        for (Class<?> c : discovery.scan()) {
            count++;
            System.out.println(c);
        }
        Assert.assertEquals(9, count);
    }

    @Test
    public void test_large() throws Exception {
        AnnotatedPluginDiscovery discovery = new AnnotatedPluginDiscovery(Retention.class, new DefaultLoader());
        for (Class<?> aClass : discovery.scan()) ;
        //System.out.println(aClass);
    }

    @Test
    public void test_large_exclude() throws Exception {
        AnnotatedPluginDiscovery discovery = new AnnotatedPluginDiscovery(Retention.class, new DefaultLoader());
        discovery.excludePackages("java", "javax", "sun", "sunw", "com.sun", "com.google");
        for (Class<?> aClass : discovery.scan()) ;
        //System.out.println(aClass);
    }

}