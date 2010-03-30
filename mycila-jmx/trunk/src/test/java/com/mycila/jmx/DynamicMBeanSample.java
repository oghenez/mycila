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

import com.mycila.jmx.export.DefaultJmxExporter;
import com.mycila.jmx.export.DefaultJmxNamingStrategy;
import com.mycila.jmx.export.ExportBehavior;
import com.mycila.jmx.export.JmxExporter;
import com.mycila.jmx.export.ReflectiveJmxMetadataAssembler;

import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DynamicMBeanSample {

    public static void main(String[] args) throws Exception {
        MyObject2 object2 = new MyObject2();
        new JmxServerFactory().locateDefault().registerMBean(new StandardMBean(new MyObject2(), MyObject2MBean.class), new ObjectName("com:type=b"));

        MyObject2MBean object2MBean = (MyObject2MBean) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{MyObject2MBean.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(proxy, args);
            }
        });

        MyObject3 object3 = new MyObject3();

        JmxExporter jmxExporter = new DefaultJmxExporter(
                new JmxServerFactory().locateDefault(),
                ExportBehavior.FAIL_ON_EXISTING,
                new DefaultJmxNamingStrategy(),
                new ReflectiveJmxMetadataAssembler());

        jmxExporter.register(object3);

        new CountDownLatch(1).await();
    }

    public static class MyObject {
    }

    public static interface MyObject2MBean {
    }

    public static class MyObject2 implements MyObject2MBean {
        final String abc = "abc1";
        public final String ghi = "ghi";
    }

    public static class MyObject3 extends MyObject2 {
        public final String abc = "abc2";
        final String def = "def";
    }
}

// TODO: http://java.sun.com/javase/7/docs/api/javax/management/Descriptor.html
