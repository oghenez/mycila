/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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
package com.mycila.testing.plugin.spring;

import com.mycila.testing.core.MycilaTesting;
import static com.mycila.testing.plugin.spring.Bean.Scope.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class BeanMethodTest {

    String a = "helloa";
    String b = "hellob";

    @Autowired
    ApplicationContext injector;

    @Bean(name = "abean")
    String a() {
        return a;
    }

    @Bean(name = "bbean", scope = PROTOTYPE)
    String b() {
        return b;
    }

    @Test
    public void test_bind() {
        MycilaTesting.from(getClass()).createNotifier(this).prepare();
        assertEquals(injector.getBean("abean"), "helloa");
        assertEquals(injector.getBean("bbean"), "hellob");
        a = "changeda";
        b = "changedb";
        assertEquals(injector.getBean("abean"), "helloa");
        assertEquals(injector.getBean("bbean"), "changedb");
    }
}