package com.mycila.jmx.export;

import com.mycila.jmx.test.Code;
import org.junit.Test;

import javax.management.ObjectName;
import java.lang.reflect.Field;

import static com.mycila.jmx.test.Throws.*;
import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaJmxExporterTest {

    @Test
    public void test_beavior() throws Exception {
        final MycilaJmxExporter exporter = new MycilaJmxExporter();
        exporter.setMetadataAssembler(new PublicMetadataAssembler() {
            @Override
            protected String getAttributeExportName(Class<?> managedClass, Field attribute) {
                return attribute.getName();
            }
        });
        exporter.setExportBehavior(ExportBehavior.FAIL_ON_EXISTING);

        final ObjectName on = ObjectName.getInstance("a:type=b");

        exporter.register(new Object() {
            public String val = "0";
        }, on);
        assertTrue(exporter.getMBeanServer().isRegistered(on));
        assertEquals("0", exporter.getMBeanServer().getAttribute(on, "val"));

        assertThat(new Code() {
            public void run() throws Throwable {
                exporter.register(new Object(), on);
            }
        }, fire(JmxExportException.class, "Unable to register MBean [com.mycila.jmx.export.ContextualDynamicMBean] with object name [a:type=b]"));

        exporter.setExportBehavior(ExportBehavior.SKIP_EXISTING);
        exporter.register(new Object() {
            public String val = "1";
        }, on);
        assertTrue(exporter.getMBeanServer().isRegistered(on));
        assertEquals("0", exporter.getMBeanServer().getAttribute(on, "val"));

        exporter.setExportBehavior(ExportBehavior.REPLACE_EXISTING);
        exporter.register(new Object() {
            public String val = "1";
        }, on);
        assertTrue(exporter.getMBeanServer().isRegistered(on));
        assertEquals("1", exporter.getMBeanServer().getAttribute(on, "val"));
    }

    @Test
    public void test_registration() throws Exception {
        final MycilaJmxExporter exporter = new MycilaJmxExporter();
        exporter.setMetadataAssembler(new PublicMetadataAssembler());
        ObjectName a = exporter.register(new Object());
        assertFalse(exporter.getMBeanServer().isRegistered(ObjectName.getInstance("inexisting:type=a")));
        exporter.unregister(ObjectName.getInstance("inexisting:type=a"));
        exporter.unregister(a);
        assertFalse(exporter.getMBeanServer().isRegistered(a));
    }

    @Test
    public void test_unique() throws Exception {
        final MycilaJmxExporter exporter = new MycilaJmxExporter();
        exporter.setMetadataAssembler(new PublicMetadataAssembler());
        exporter.setEnsureUnique(true);

        Object o1 = new Object();
        Object o2 = new Object();
        ObjectName on1 = exporter.register(o1);
        ObjectName on2 = exporter.register(o2);
        assertEquals(on1.getKeyProperty("identity"), "" + Integer.toHexString(o1.hashCode()));
        assertEquals(on2.getKeyProperty("identity"), "" + Integer.toHexString(o2.hashCode()));
    }

}
