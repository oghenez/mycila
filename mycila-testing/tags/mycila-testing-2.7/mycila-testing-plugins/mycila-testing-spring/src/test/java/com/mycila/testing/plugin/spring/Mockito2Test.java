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

import com.mycila.testing.junit.MycilaJunitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(MycilaJunitRunner.class)
@SpringContext(locations = "/Mockito2Test.xml")
public final class Mockito2Test {

    @Mock
    @Bean
    private CategoryService categoryService;

    @Autowired
    private CategoryAdminViewController controller;

    @Test
    public void test() {
        /*ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/MockitoTest.xml");
        assertNotNull(ctx.getBean("categoryService"));
        assertNotNull(ctx.getBean("categoryAdminViewController"));
        assertNotNull(((CategoryAdminViewController) ctx.getBean("categoryAdminViewController")).categoryService);
        assertEquals("default implementation", ((CategoryAdminViewController) ctx.getBean("categoryAdminViewController")).categoryService.name());*/

        when(categoryService.name()).thenReturn("mocked implementation");

        assertEquals("mocked implementation", categoryService.name());
        assertEquals("mocked implementation", controller.categoryService.name());
    }
}