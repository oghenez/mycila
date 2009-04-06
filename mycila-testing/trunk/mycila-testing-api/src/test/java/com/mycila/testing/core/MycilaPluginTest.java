package com.mycila.testing.core;

import com.mycila.testing.MyPlugin;
import static com.mycila.testing.core.Cache.*;
import com.mycila.testing.util.ExtendedAssert;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@MycilaPlugins(cache =  UNSHARED, descriptor = "")
public final class MycilaPluginTest {

    @Test
    public void test_share_plugins() throws Exception {
        MycilaTesting mycilaTesting = MycilaTesting.from(new MycilaPlugins() {
            public Cache cache() {
                return SHARED;
            }

            public String descriptor() {
                return MycilaTesting.DEFAULT_PLUGIN_DESCRIPTOR;
            }

            public Class<? extends Annotation> annotationType() {
                return MycilaPlugins.class;
            }
        });
        assertEquals(mycilaTesting.pluginManager().getCache().getBindings().size(), 1);
        assertTrue(mycilaTesting.pluginManager().getCache().contains("myplugin"));
        assertEquals(mycilaTesting, MycilaTesting.from(new MycilaPlugins() {
            public Cache cache() {
                return SHARED;
            }

            public String descriptor() {
                return MycilaTesting.DEFAULT_PLUGIN_DESCRIPTOR;
            }

            public Class<? extends Annotation> annotationType() {
                return MycilaPlugins.class;
            }
        }));
        assertEquals(mycilaTesting, MycilaTesting.from(new MycilaPlugins() {
            public Cache cache() {
                return null;
            }

            public String descriptor() {
                return MycilaTesting.DEFAULT_PLUGIN_DESCRIPTOR;
            }

            public Class<? extends Annotation> annotationType() {
                return MycilaPlugins.class;
            }
        }));
        assertEquals(mycilaTesting, MycilaTesting.from((MycilaPlugins) null));
        assertEquals(mycilaTesting, MycilaTesting.from((Class<?>) null));
    }

    @Test
    public void test_custom() throws Exception {
        MycilaTesting mycilaTesting = MycilaTesting.from(MycilaPluginTest.class);
        assertEquals(mycilaTesting.pluginManager().getCache().getBindings().size(), 0);
        mycilaTesting = MycilaTesting.from(new MycilaPlugins() {
            public Cache cache() {
                return SHARED;
            }

            public String descriptor() {
                return "";
            }

            public Class<? extends Annotation> annotationType() {
                return MycilaPlugins.class;
            }
        });
        mycilaTesting.pluginManager().getCache().registerPlugin("aa", new MyPlugin());
        MycilaTesting mycilaTesting2 = MycilaTesting.from(new MycilaPlugins() {
            public Cache cache() {
                return SHARED;
            }

            public String descriptor() {
                return null;
            }

            public Class<? extends Annotation> annotationType() {
                return MycilaPlugins.class;
            }
        });
        assertEquals(mycilaTesting, mycilaTesting2);
        assertTrue(mycilaTesting2.pluginManager().getCache().contains("aa"));
    }

    @Test
    public void test_plugins_per_test() throws Exception {

        MycilaTesting mycilaTesting = MycilaTesting.from(new MycilaPlugins() {
            public Cache cache() {
                return UNSHARED;
            }

            public String descriptor() {
                return "/plugins.properties";
            }

            public Class<? extends Annotation> annotationType() {
                return MycilaPlugins.class;
            }
        });
        assertEquals(mycilaTesting.pluginManager().getCache().getBindings().size(), 1);
        assertTrue(mycilaTesting.pluginManager().getCache().contains("myplugin"));
        ExtendedAssert.assertNotEquals(mycilaTesting, MycilaTesting.from(new MycilaPlugins() {
            public Cache cache() {
                return UNSHARED;
            }

            public String descriptor() {
                return "/plugins.properties";
            }

            public Class<? extends Annotation> annotationType() {
                return MycilaPlugins.class;
            }
        }));
    }

}
