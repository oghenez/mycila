package com.mycila.testing.plugin.spring;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Field;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class FieldAccessFactoryBean implements FactoryBean{

    private final Object test;
    private final Field field;

    FieldAccessFactoryBean(Object test, Field field) {
        this.test = test;
        this.field = field;
    }

    public Object getObject() throws Exception {
        return field.get(test);
    }

    public Class getObjectType() {
        return field.getType();
    }

    public boolean isSingleton() {
        return field.getAnnotation(Bean.class).scope() == Bean.Scope.SINGLETON;
    }
}
