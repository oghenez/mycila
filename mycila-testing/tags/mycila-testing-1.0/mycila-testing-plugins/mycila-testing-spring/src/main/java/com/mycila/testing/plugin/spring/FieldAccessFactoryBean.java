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
