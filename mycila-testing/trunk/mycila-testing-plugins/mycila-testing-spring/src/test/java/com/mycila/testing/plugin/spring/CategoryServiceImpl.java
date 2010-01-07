package com.mycila.testing.plugin.spring;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Service("categoryService")
public final class CategoryServiceImpl implements CategoryService
{
    public String name() {
        return "default implementation";
    }
}
