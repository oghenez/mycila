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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class MyTestTest {

    @Test
    public void test() throws Exception {
        String cName = "com.mycila.jmx.spring.SelfNaming";
        URLClassLoader loader = new URLClassLoader(new URL[]{new File("target/classes").toURI().toURL()}, ClassLoader.getSystemClassLoader().getParent());
        Class<?> clazz1 = Class.forName(cName);
        Class<?> clazz11 = Class.forName(cName);
        Class<?> clazz2 = loader.loadClass(cName);
        Class<?> clazz22 = loader.loadClass(cName);

        System.out.println(clazz1 == clazz11);
        System.out.println(clazz1.equals(clazz11));

        System.out.println(clazz2 == clazz22);
        System.out.println(clazz2.equals(clazz22));

        System.out.println(clazz1 == clazz2);
        System.out.println(clazz1.equals(clazz2));
    }

}
