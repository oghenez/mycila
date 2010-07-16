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

package test;

import com.mycila.plugin.annotation.scope.ExpiringSingleton;
import com.mycila.plugin.spi.internal.Defaults;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */

final class Main {

    public static void main(String[] args) throws Exception {
        ExpiringSingleton e = (ExpiringSingleton) Proxy.newProxyInstance(Main.class.getClassLoader(), new Class[]{ExpiringSingleton.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method);
                return Defaults.defaultValue(method.getReturnType());
            }
        });

        ExpiringSingleton e2 = Main.class.getMethod("test1").getAnnotation(ExpiringSingleton.class);
        ExpiringSingleton e3 = Main.class.getMethod("test2").getAnnotation(ExpiringSingleton.class);
        System.out.println(e);
        System.out.println(e2);
        System.out.println(e3);
        System.out.println(e2.equals(e3));
        System.out.println(e.equals(e2));
    }

    @ExpiringSingleton(0l)
    public static void test1() {
    }

    @ExpiringSingleton(0l)
    public static void test2() {
    }
}
