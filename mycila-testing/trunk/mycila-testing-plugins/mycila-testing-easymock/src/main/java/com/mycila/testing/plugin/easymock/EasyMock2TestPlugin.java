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

package com.mycila.testing.plugin.easymock;

import com.mycila.testing.core.AbstractTestPlugin;
import com.mycila.testing.core.Context;
import com.mycila.testing.core.TestPluginException;
import org.easymock.classextension.EasyMock;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class EasyMock2TestPlugin extends AbstractTestPlugin {

    @Override
    public List<String> getAfter() {
        return Arrays.asList("spring", "guice1", "guice2");
    }

    public void prepareTestInstance(Context context) {
        Field[] mocks = context.getTest().getFieldsAnnotatedWith(Mock.class);
        for (Field field : mocks) {
            context.getTest().set(field, mock(field));
        }
    }

    private Object mock(Field field) {
        String name = field.getDeclaringClass().getName().replaceAll(".", "_") + "_" + field.getName();
        switch (field.getAnnotation(Mock.class).value()) {
            case STANDARD:
                return EasyMock.createMock(name, field.getType());
            case NICE:
                return EasyMock.createNiceMock(name, field.getType());
            case STRICT:
                return EasyMock.createStrictMock(name, field.getType());
        }
        throw new TestPluginException("Invalid mock type for field '%s': %s", field, field.getAnnotation(Mock.class).value());
    }
}
