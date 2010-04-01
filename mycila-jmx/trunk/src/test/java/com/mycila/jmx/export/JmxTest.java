package com.mycila.jmx.export;

import com.mycila.jmx.Mx4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public abstract class JmxTest {

    protected final MBeanServer server = ManagementFactory.getPlatformMBeanServer();

    private JmxExporter jmxExporter;
    private final Collection<ObjectName> objectNames = new HashSet<ObjectName>();

    @BeforeClass
    public static void startMX4J() throws Exception {
        Mx4j.start();
    }

    @AfterClass
    public static void stopMX4J() throws Exception {
        Mx4j.stop();
    }

    @Before
    public final void setup() {
        jmxExporter = new MycilaJmxExporter();
        ((MycilaJmxExporter) jmxExporter).setMetadataAssembler(getMetadataAssembler());
    }

    @After
    public final void clean() {
        for (Iterator<ObjectName> it = objectNames.iterator(); it.hasNext(); it.remove())
            jmxExporter.unregister(it.next());
    }

    protected final ObjectName register(Object o) {
        ObjectName objectName = jmxExporter.register(o);
        objectNames.add(objectName);
        return objectName;
    }

    protected abstract JmxMetadataAssembler getMetadataAssembler();

}
