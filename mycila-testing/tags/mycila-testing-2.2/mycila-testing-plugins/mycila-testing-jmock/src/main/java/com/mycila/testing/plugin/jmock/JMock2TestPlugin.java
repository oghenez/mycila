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

package com.mycila.testing.plugin.jmock;

import com.mycila.testing.core.api.TestContext;
import static com.mycila.testing.core.introspect.Filters.*;
import com.mycila.testing.core.plugin.DefaultTestPlugin;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JMock2TestPlugin extends DefaultTestPlugin {

    public static final String MOCKERY = "org.jmock.Mockery";

    @Override
    public List<String> getAfter() {
        return Arrays.asList("spring", "guice1", "guice2");
    }

    @Override
    public void prepareTestInstance(TestContext context) {
        List<Field> mocks = context.introspector().selectFields(fieldsAnnotatedBy(Mock.class));
        Mockery mockery = findProvidedMockery(context);
        if (mockery == null) {
            mockery = new Mockery();
            for (Field mock : mocks) {
                if (!mock.getType().isInterface()) {
                    mockery.setImposteriser(ClassImposteriser.INSTANCE);
                    break;
                }
            }
        }
        context.attributes().set(MOCKERY, mockery);
        for (Field field : context.introspector().selectFields(and(fieldsAccepting(Mockery.class), fieldsAnnotatedBy(MockContext.class)))) {
            context.introspector().set(field, mockery);
        }
        for (Field field : mocks) {
            context.introspector().set(field, mockery.mock(field.getType(), field.getDeclaringClass().getName() + "." + field.getName()));
        }
    }

    private Mockery findProvidedMockery(TestContext context) {
        {
            List<Method> methods = context.introspector().selectMethods(excludeOverridenMethods(methodsAnnotatedBy(MockContextProvider.class)));
            if (methods.size() > 0) {
                Object o = context.introspector().invoke(methods.get(0));
                if (o != null && o instanceof Mockery) {
                    return (Mockery) o;
                }
                throw new IllegalArgumentException(String.format("Method '%s' annotated with @MockContextProvider did not returned a valid Mockery object: %s", methods.get(0), o));
            }
        }
        {
            List<Field> fields = context.introspector().selectFields(fieldsAnnotatedBy(MockContextProvider.class));
            if (fields.size() > 0) {
                Object o = context.introspector().get(fields.get(0));
                if (o != null && o instanceof Mockery) {
                    return (Mockery) o;
                }
                throw new IllegalArgumentException(String.format("Field '%s' annotated with @MockContextProvider does not have a valid Mockery object: %s", fields.get(0), o));
            }
        }
        return null;
    }

}
