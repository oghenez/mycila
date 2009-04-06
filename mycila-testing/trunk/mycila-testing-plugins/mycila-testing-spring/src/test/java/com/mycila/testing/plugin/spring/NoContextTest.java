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
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class NoContextTest {

    @Autowired
    ApplicationContext beanFactory;

    @Test
    public void test_setup() {
        assertNull(beanFactory);
        MycilaTesting.from(getClass()).createNotifier(this).prepare();
        assertNotNull(beanFactory);
        assertFalse(beanFactory.containsBean("myBean"));
    }
}