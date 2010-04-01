package com.mycila.jmx;

import mx4j.tools.adaptor.http.HttpAdaptor;
import mx4j.tools.adaptor.http.XSLTProcessor;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Mx4j {

    private static HttpAdaptor httpAdaptor;

    static {
        try {
            httpAdaptor = new HttpAdaptor(8080, "localhost");
            httpAdaptor.setProcessor(new XSLTProcessor());
            ManagementFactory.getPlatformMBeanServer().registerMBean(httpAdaptor, ObjectName.getInstance("mx4j:type=HttpAdaptor"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void start() throws Exception {
        if (!httpAdaptor.isActive())
            httpAdaptor.start();
    }

    public static void stop() {
        if (httpAdaptor.isActive())
            httpAdaptor.stop();
    }
}
