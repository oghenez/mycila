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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import static com.mycila.testing.plugin.spring.Bean.Scope.*;
import static org.testng.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class BeanField2Test {

    @Bean
    String abean = "helloa";

    @Bean(scope = PROTOTYPE)
    String bbean = "hellob";

    @Autowired
    ApplicationContext injector;

    @Test
    public void test_bind() {
        MycilaTesting.from(getClass()).createNotifier(this).prepare();
        assertEquals(injector.getBean("abean"), "helloa");
        assertEquals(injector.getBean("bbean"), "hellob");
        abean = "changeda";
        bbean = "changedb";
        assertEquals(injector.getBean("abean"), "helloa");
        assertEquals(injector.getBean("bbean"), "changedb");
    }
}