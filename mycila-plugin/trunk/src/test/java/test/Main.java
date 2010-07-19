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

import com.mycila.plugin.Plugin;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.interceptor.SimpleTraceInterceptor;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */

final class Main {

    public static void main(String[] args) throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTargetClass(Plugin.class);
        proxyFactory.setInterfaces(new Class[]{Plugin.class});
        proxyFactory.addAdvice(new SimpleTraceInterceptor());
        System.out.println(proxyFactory.getProxy());
    }

}
