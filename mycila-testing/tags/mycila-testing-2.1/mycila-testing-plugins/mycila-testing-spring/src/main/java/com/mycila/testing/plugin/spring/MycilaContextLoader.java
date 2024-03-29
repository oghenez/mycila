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

import com.mycila.testing.core.api.TestContext;
import static com.mycila.testing.core.introspect.Filters.*;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.GenericXmlContextLoader;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class MycilaContextLoader extends GenericXmlContextLoader {
    private final TestContext mycilaContext;

    public MycilaContextLoader(TestContext mycilaContext) {
        this.mycilaContext = mycilaContext;
    }

    public String[] contextLocations() {
        SpringContext ctx = mycilaContext.introspector().testClass().getAnnotation(SpringContext.class);
        return ctx == null ? new String[0] : ctx.locations();
    }

    @Override
    protected void customizeContext(GenericApplicationContext context) {
        for (Field field : mycilaContext.introspector().selectFields(fieldsAnnotatedBy(Bean.class))) {
            Bean annotation = field.getAnnotation(Bean.class);
            context.registerBeanDefinition(
                    annotation.name(),
                    createBeanDefinition(field, FieldAccessFactoryBean.class, annotation.scope()));
        }
        for (Method method : mycilaContext.introspector().selectMethods(excludeOverridenMethods(methodsAnnotatedBy(Bean.class)))) {
            Bean annotation = method.getAnnotation(Bean.class);
            context.registerBeanDefinition(
                    annotation.name(),
                    createBeanDefinition(method, MethodAccessFactoryBean.class, annotation.scope()));
        }
        context.registerBeanDefinition(
                "org.springframework.test.context.TestContext",
                createBeanDefinition(mycilaContext.attributes().get("org.springframework.test.context.TestContext"), ObjectFactoryBean.class));
    }

    private AbstractBeanDefinition createBeanDefinition(Object object, Class beanClass) {
        ConstructorArgumentValues args = new ConstructorArgumentValues();
        args.addIndexedArgumentValue(0, object);
        GenericBeanDefinition beanDef = new GenericBeanDefinition();
        beanDef.setBeanClass(beanClass);
        beanDef.setScope(Bean.Scope.SINGLETON.value());
        beanDef.setAutowireCandidate(true);
        beanDef.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_AUTODETECT);
        beanDef.setConstructorArgumentValues(args);
        return beanDef;
    }

    private AbstractBeanDefinition createBeanDefinition(AccessibleObject access, Class beanClass, Bean.Scope scope) {
        ConstructorArgumentValues args = new ConstructorArgumentValues();
        args.addIndexedArgumentValue(0, mycilaContext.introspector().instance());
        args.addIndexedArgumentValue(1, access);
        GenericBeanDefinition beanDef = new GenericBeanDefinition();
        beanDef.setBeanClass(beanClass);
        beanDef.setScope(scope.value());
        beanDef.setAutowireCandidate(true);
        beanDef.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_AUTODETECT);
        beanDef.setConstructorArgumentValues(args);
        return beanDef;
    }
}
