package com.mycila.testing.plugin.spring;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class MethodAccessFactoryBean implements FactoryBean {

    private final Object test;
    private final Method method;

    MethodAccessFactoryBean(Object test, Method method) {
        this.test = test;
        this.method = method;
    }

    public Object getObject() throws Exception {
        return method.invoke(test);
    }

    public Class getObjectType() {
        return method.getReturnType();
    }

    public boolean isSingleton() {
        return method.getAnnotation(Bean.class).scope() == Bean.Scope.SINGLETON;
    }
}