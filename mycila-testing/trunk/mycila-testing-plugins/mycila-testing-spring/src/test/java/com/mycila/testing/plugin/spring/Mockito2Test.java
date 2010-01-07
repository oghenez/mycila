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