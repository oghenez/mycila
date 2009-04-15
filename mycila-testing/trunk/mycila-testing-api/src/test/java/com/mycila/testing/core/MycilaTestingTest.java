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
package com.mycila.testing.core;

import com.mycila.testing.core.annot.MycilaPlugins;
import com.mycila.testing.core.api.Cache;
import static com.mycila.testing.core.api.Cache.*;
import com.mycila.testing.ea.ExtendedAssert;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@MycilaPlugins(value = UNSHARED, descriptor = "")
public final class MycilaTestingTest {

    @Test
    public void test_share_plugins() throws Exception {
        MycilaTesting mycilaTesting = MycilaTesting.from(new MycilaPlugins() {
            public Cache value() {
                return SHARED;
            }

            public String descriptor() {
                return MycilaTesting.DEFAULT_PLUGIN_DESCRIPTOR;
            }

            public Class<? extends Annotation> annotationType() {
                throw new AssertionError("should not go here");
            }
        });
        assertEquals(mycilaTesting.pluginManager().getCache().getBindings().size(), 1);
        assertTrue(mycilaTesting.pluginManager().getCache().contains("myplugin"));
        assertEquals(mycilaTesting, MycilaTesting.from(new MycilaPlugins() {
            public Cache value() {
                return SHARED;
            }

            public String descriptor() {
                return MycilaTesting.DEFAULT_PLUGIN_DESCRIPTOR;
            }

            public Class<? extends Annotation> annotationType() {
                throw new AssertionError("should not go here");
            }
        }));
    }

    @Test
    public void test_custom() throws Exception {
        MycilaTesting mycilaTesting = MycilaTesting.from(MycilaTestingTest.class);
        assertEquals(mycilaTesting.pluginManager().getCache().getBindings().size(), 0);
        mycilaTesting = MycilaTesting.from(new MycilaPlugins() {
            public Cache value() {
                return SHARED;
            }

            public String descriptor() {
                return "";
            }

            public Class<? extends Annotation> annotationType() {
                throw new AssertionError("should not go here");
            }
        });
        mycilaTesting.pluginManager().getCache().registerPlugin("aa", new MyPlugin());
        MycilaTesting mycilaTesting2 = MycilaTesting.from(new MycilaPlugins() {
            public Cache value() {
                return SHARED;
            }

            public String descriptor() {
                return null;
            }

            public Class<? extends Annotation> annotationType() {
                throw new AssertionError("should not go here");
            }
        });
        assertEquals(mycilaTesting, mycilaTesting2);
        assertTrue(mycilaTesting2.pluginManager().getCache().contains("aa"));
    }

    @Test
    public void test_plugins_per_test() throws Exception {

        MycilaTesting mycilaTesting = MycilaTesting.from(new MycilaPlugins() {
            public Cache value() {
                return UNSHARED;
            }

            public String descriptor() {
                return "/plugins.properties";
            }

            public Class<? extends Annotation> annotationType() {
                throw new AssertionError("should not go here");
            }
        });
        assertEquals(mycilaTesting.pluginManager().getCache().getBindings().size(), 1);
        assertTrue(mycilaTesting.pluginManager().getCache().contains("myplugin"));
        ExtendedAssert.assertNotEquals(mycilaTesting, MycilaTesting.from(new MycilaPlugins() {
            public Cache value() {
                return UNSHARED;
            }

            public String descriptor() {
                return "/plugins.properties";
            }

            public Class<? extends Annotation> annotationType() {
                throw new AssertionError("should not go here");
            }
        }));
    }

}
