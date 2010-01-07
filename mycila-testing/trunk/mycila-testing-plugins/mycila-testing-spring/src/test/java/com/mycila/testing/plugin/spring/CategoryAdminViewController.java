package com.mycila.testing.plugin.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Controller("categoryAdminViewController")
public final class CategoryAdminViewController {

    @Autowired
    @Qualifier("categoryService")
    CategoryService categoryService;

}
