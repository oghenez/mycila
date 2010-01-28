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

package com.mycila.ujd;

import com.mycila.ujd.api.Analyzer;
import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.JVMUpdater;
import com.mycila.ujd.impl.DefaultJVM;
import com.mycila.ujd.impl.DefaultJVMUpdater;
import com.mycila.ujd.impl.MycilaUJDAnalyzer;
import com.mycila.ujd.mbean.JmxAnalyzer;
import com.mycila.ujd.mbean.JmxUpdater;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.Vector;
import java.util.jar.JarFile;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaUJD {

    private static JVMUpdater updater;

    public static void premain(String agentArgs, Instrumentation instrumentation) throws Exception {
        agentmain(agentArgs, instrumentation);
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) throws Exception {
        System.out.println("Mycila Unnecessary JAR Detector loaded !");
        int interval = 20;
        if(agentArgs != null) {
            int pos = agentArgs.indexOf("interval=");
            if (pos != -1) interval = Integer.parseInt(agentArgs.substring(pos + 9));
        }
        JVM jvm = new DefaultJVM();
        Analyzer analyzer = new MycilaUJDAnalyzer(jvm);
        updater = new DefaultJVMUpdater(jvm, instrumentation);
        register("Mycila UJD:name=Analyzer", new JmxAnalyzer(analyzer));
        register("Mycila UJD:name=Updater", new JmxUpdater(updater));
        updater.start(interval);
    }

    private static void register(String objectName, Object o) throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName on = new ObjectName(objectName);
        try {
            if (server.isRegistered(on))
                server.unregisterMBean(on);
        } catch (Exception ignored) {
        }
        server.registerMBean(o, on);
    }

    // only for testing purposes

    public static void main(String... args) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) sb.append(arg).append(";");
        agentmain(sb.toString(), new Instrumentation() {
            public void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {
            }

            public boolean isRetransformClassesSupported() {
                return false;
            }

            public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {
            }

            public boolean isModifiableClass(Class<?> theClass) {
                return false;
            }

            public void appendToBootstrapClassLoaderSearch(JarFile jarfile) {
            }

            public void appendToSystemClassLoaderSearch(JarFile jarfile) {
            }

            public boolean isNativeMethodPrefixSupported() {
                return false;
            }

            public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {
            }

            public void addTransformer(ClassFileTransformer transformer) {
            }

            public boolean removeTransformer(ClassFileTransformer transformer) {
                return false;
            }

            public boolean isRedefineClassesSupported() {
                return false;
            }

            public void redefineClasses(ClassDefinition[] definitions) throws ClassNotFoundException, UnmodifiableClassException {
            }

            public Class[] getAllLoadedClasses() {
                try {
                    ClassLoader cl = ClassLoader.getSystemClassLoader();
                    Field classes = ClassLoader.class.getDeclaredField("classes");
                    classes.setAccessible(true);
                    Vector<Class<?>> vector = (Vector<Class<?>>) classes.get(cl);
                    return vector.toArray(new Class[vector.size()]);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

            public Class[] getInitiatedClasses(ClassLoader loader) {
                return new Class[0];
            }

            public long getObjectSize(Object objectToSize) {
                return 0;
            }
        });
        updater.await();
    }
}
