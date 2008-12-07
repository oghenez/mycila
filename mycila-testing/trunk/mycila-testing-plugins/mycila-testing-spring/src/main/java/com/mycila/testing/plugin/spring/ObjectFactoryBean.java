package com.mycila.testing.plugin.spring;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ObjectFactoryBean implements FactoryBean {

    private final Object object;

    ObjectFactoryBean(Object object) {
        this.object = object;
    }

    public Object getObject() throws Exception {
        return object;
    }

    public Class getObjectType() {
        return object.getClass();
    }

    public boolean isSingleton() {
        return true;
    }
}