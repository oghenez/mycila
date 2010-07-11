package com.mycila.plugin.discovery;

import com.google.common.collect.Iterables;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class JdkServicePluginDiscoveryTest {

    @Test
    public void test() throws Exception {
        JdkServicePluginDiscovery discovery = new JdkServicePluginDiscovery(Serv.class);
        assertEquals(2, Iterables.size(discovery.scan()));
    }

}
