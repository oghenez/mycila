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

package com.mycila.jmx;

import com.mycila.jmx.export.ExportBehavior;
import com.mycila.jmx.export.JmxExporter;
import com.mycila.jmx.export.MBeanNamingStrategy;
import com.mycila.jmx.export.MycilaJmxExporter;
import com.mycila.jmx.export.ReflectionMetadataAssembler;
import mx4j.tools.adaptor.http.HttpAdaptor;
import mx4j.tools.adaptor.http.XSLTProcessor;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ReflectionSample {

    public static void main(String[] args) throws Exception {
        JmxExporter jmxExporter = new MycilaJmxExporter(
                new JmxServerFactory().locateDefault(),
                ExportBehavior.FAIL_ON_EXISTING,
                new MBeanNamingStrategy(),
                new ReflectionMetadataAssembler());

        MyObject3 object3 = new MyObject3();
        jmxExporter.register(object3);

        HttpAdaptor httpAdaptor = new HttpAdaptor(80, "localhost");
        httpAdaptor.setProcessor(new XSLTProcessor());
        ManagementFactory.getPlatformMBeanServer().registerMBean(httpAdaptor, ObjectName.getInstance("mx4j:type=HttpAdaptor"));
        httpAdaptor.start();

        //ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext("/spring.xml");

        new CountDownLatch(1).await();
    }

    public static class MyObject {
        public final String abc = "abc1";
        @Deprecated
        public final String ghi = "ghi";

        @Deprecated
        public void setName(String name) {
        }

        public void setText(CharSequence c) {
        }

        public CharSequence getText() {
            return "hello UP";
        }

        private String getAbc() {
            return abc;
        }
    }

    @ManagedResource(objectName = "bean:name=testBean4", description = "My Managed Bean")
    public static class MyObject3 extends MyObject {
        public String abc = "abc2";
        String def = "def";
        public final String jkl = "jkl";

        public String getJkl() {
            return jkl;
        }

        public String getAbc() {
            return abc;
        }

        public void setAbc(String abc) {
            this.abc = abc;
        }

        public void start() {
        }

        public void isA() {
        }

        public void setA() {
        }

        public void getA() {
        }

        public int getVal() {
            return 0;
        }

        @Deprecated
        public void setVal(int val) {
        }

        @ManagedAttribute(description = "Name")
        public String getName() {
            return "hello";
        }

        @Deprecated
        @ManagedAttribute(description = "Text")
        public String getText() {
            return "hello DOWN";
        }

        @ManagedAttribute(description = "Text")
        public void setText(String c) {
        }

    }
}
