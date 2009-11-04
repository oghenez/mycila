package com.mycila.sandbox.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class InjectableObjectFactory<T> extends AbstractFactoryBean implements ApplicationContextAware {

    private ApplicationContext context;
    @SuppressWarnings({"unchecked"})
    private Class<T>[] classes = new Class[0];

    public void setClasses(Class<T>[] classes) {
        this.classes = classes;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public Class<?> getObjectType() {
        return Object[].class;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected Object createInstance() throws Exception {
        AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
        List<T> instances = new LinkedList<T>();
        for (Class<T> clazz : getClasses())
            instances.add((T) autowireCapableBeanFactory.createBean(clazz, AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT, true));
        return instances.toArray(new Object[instances.size()]);
    }

    public Class<T>[] getClasses() {
        return classes;
    }
}