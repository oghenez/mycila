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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@SpringContext(locations = "/ctx.xml")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional
public final class ContextConfigTest {

    @Autowired
    ApplicationContext beanFactory;

    @Autowired
    TestContext testContext;

    @Autowired
    @Qualifier("myBean1")
    MyBean a;

    @Autowired
    @Qualifier("myBean2")
    MyBean b;

    @Test
    public void test_setup() {
        assertNull(a);
        assertNull(b);
        assertNull(beanFactory);
        assertNull(testContext);
        TestSetup.staticDefaultSetup().prepare(this);
        assertNotNull(a);
        assertNotNull(b);
        assertNotNull(testContext);
        assertNotNull(beanFactory);
        assertNotNull(beanFactory.getBean("myBean1"));
        assertEquals(a.txt, "myBean1");
        assertEquals(b.txt, "myBean2");
    }
}
