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

import com.google.common.base.Predicates;
import com.mycila.ujd.api.ContainedClass;
import com.mycila.ujd.api.ContainedJavaClass;
import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.JavaClass;
import com.mycila.ujd.api.Loader;
import com.mycila.ujd.impl.DefaultJVM;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Run with: -javaagent:./target/mycila-ujd-1.0-SNAPSHOT.jar
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class UjdSample {
    public static void main(String... args) throws Exception {
        URLClassLoader cl = new URLClassLoader(
                new URL[]{
                        //new URL(new URL("http://repo2.maven.org/maven2/com/mycila/mycila-log/2.9/mycila-log-2.9.jar"), "jar:http://repo2.maven.org/maven2/com/mycila/mycila-log/2.9/mycila-log-2.9.jar!/"),
                        //new URL("jar:http://repo2.maven.org/maven2/com/mycila/mycila-log/2.9/mycila-log-2.9.jar!/"),
                        new URL("http://repo2.maven.org/maven2/log4j/log4j/1.2.15/log4j-1.2.15.jar"),
                        new File("target/classes/").toURI().toURL(),
                        new File("target/test-classes/").toURI().toURL()},
                ClassLoader.getSystemClassLoader().getParent());

        Class<?> remote1 = cl.loadClass("org.apache.log4j.Level");
        Class<?> remote2 = cl.loadClass("com.mycila.ujd.api.Loader");

        JVM jvm = new DefaultJVM();
        jvm.addClasses(remote1, remote2);
        for (JavaClass<?> loadedClass : jvm.getClasses()) {
            System.out.println(loadedClass);
            for (Container container : loadedClass.getLoader().getContainers())
                for (ContainedClass aClass : container.getClasses())
                    System.out.println(aClass);
        }

        jvm.addClasses(UjdSample.class, Loader.class, Predicates.class);
        for (ContainedJavaClass<?> loadedClass : jvm.<ContainedJavaClass<?>>getClasses(Predicates.instanceOf(ContainedJavaClass.class))) {
            System.out.println(loadedClass);
            System.out.println(loadedClass.getLoader());
            System.out.println(loadedClass.getContainer());
            for (ContainedClass containedClass : loadedClass.getContainer().getClasses())
                System.out.println(containedClass);
        }
    }
}
