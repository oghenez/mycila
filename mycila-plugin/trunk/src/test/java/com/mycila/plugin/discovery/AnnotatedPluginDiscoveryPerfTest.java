package com.mycila.plugin.discovery;

import com.google.common.collect.Iterables;
import com.mycila.plugin.ShowDurationRule;
import com.mycila.plugin.util.ClassUtils;
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
    public ShowDurationRule showDurationRule = new ShowDurationRule();

    @Test
    public void test_local() throws Exception {
        AnnotatedPluginDiscovery discovery = new AnnotatedPluginDiscovery(Retention.class, ClassUtils.getDefaultClassLoader());
        discovery.includePackages("com.mycila.plugin.annotation");
        Iterable<? extends Class<?>> it = discovery.scan();
        System.out.println(Iterables.toString(it));
        assertEquals(10, Iterables.size(it));
    }

    @Test
    public void test_large() throws Exception {
        AnnotatedPluginDiscovery discovery = new AnnotatedPluginDiscovery(Retention.class, ClassUtils.getDefaultClassLoader());
        for (Class<?> aClass : discovery.scan()) ;
        //System.out.println(aClass);
    }

    @Test
    public void test_large_exclude() throws Exception {
        AnnotatedPluginDiscovery discovery = new AnnotatedPluginDiscovery(Retention.class, ClassUtils.getDefaultClassLoader());
        discovery.excludePackages("com.sun", "com.google");
        for (Class<?> aClass : discovery.scan()) ;
        //System.out.println(aClass);
    }

}
