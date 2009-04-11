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

import com.mycila.testing.core.Context;
import com.mycila.testing.core.DefaultTestPlugin;
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

    public static final String CTX_MOCKERY = "org.jmock.Mockery";

    @Override
    public List<String> getAfter() {
        return Arrays.asList("spring", "guice1", "guice2");
    }

    @Override
    public void prepareTestInstance(Context context) {
        Field[] mocks = context.test().findFieldsAnnotatedWith(Mock.class);
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
        context.setAttribute(CTX_MOCKERY, mockery);
        for (Field field : context.test().findFieldsOfTypeAnnotatedWith(Mockery.class, MockContext.class)) {
            context.test().set(field, mockery);
        }
        for (Field field : mocks) {
            context.test().set(field, mockery.mock(field.getType(), field.getDeclaringClass().getName() + "." + field.getName()));
        }
    }

    private Mockery findProvidedMockery(Context context) {
        {
            Method[] methods = context.test().findMethodsAnnotatedWith(MockContextProvider.class);
            if (methods.length > 0) {
                Object o = context.test().invoke(methods[0]);
                if (o != null && o instanceof Mockery) {
                    return (Mockery) o;
                }
                throw new IllegalArgumentException(String.format("Method '%s' annotated with @MockContextProvider did not returned a valid Mockery object: %s", methods[0], o));
            }
        }
        {
            Field[] fields = context.test().findFieldsAnnotatedWith(MockContextProvider.class);
            if (fields.length > 0) {
                Object o = context.test().get(fields[0]);
                if (o != null && o instanceof Mockery) {
                    return (Mockery) o;
                }
                throw new IllegalArgumentException(String.format("Field '%s' annotated with @MockContextProvider does not have a valid Mockery object: %s", fields[0], o));
            }
        }
        return null;
    }

}
