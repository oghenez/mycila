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

import com.mycila.testing.core.TestSetup;
import static com.mycila.testing.plugin.spring.Bean.Scope.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@SpringContext(locations = "/ctx.xml")
public final class BeanOverrideTest {

    @Bean(name = "myBean1")
    MyBean myBean1 = new MyBean("toto");

    @Bean(name = "myBean2", scope = PROTOTYPE)
    MyBean myBean = new MyBean("tata");

    @Autowired
    ApplicationContext injector;

    @Test
    public void test_bind() {
        TestSetup.setup(this);
        assertEquals(injector.getBean("myBean1").toString(), "toto");
        assertEquals(injector.getBean("myBean2").toString(), "tata");
    }
}