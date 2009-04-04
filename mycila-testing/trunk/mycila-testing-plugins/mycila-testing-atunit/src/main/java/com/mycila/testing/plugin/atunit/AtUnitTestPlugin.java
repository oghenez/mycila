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

package com.mycila.testing.plugin.atunit;

import atunit.Mock;
import atunit.NoUnitException;
import atunit.TooManyUnitsException;
import atunit.Unit;
import atunit.core.Container;
import atunit.core.IncompatibleAnnotationException;
import atunit.core.MockFramework;
import atunit.core.NoContainer;
import atunit.core.NoMockFramework;
import atunit.lib.com.google.common.collect.Sets;
import com.mycila.testing.core.Context;
import com.mycila.testing.core.DefaultTestPlugin;
import com.mycila.testing.core.TestPluginException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AtUnitTestPlugin extends DefaultTestPlugin {

    @Override
    public void prepareTestInstance(Context context) {

        try {
            Class<?> c = context.getTest().getTargetClass();
            Set<Field> testFields = getFields(c);

            Container container = getContainerFor(c);
            MockFramework mockFramework = getMockFrameworkFor(c);

            // make sure we have one (and only one) @Unit field
            Field unitField = getUnitField(testFields);
            if (unitField.getAnnotation(Mock.class) != null) {
                throw new IncompatibleAnnotationException(Unit.class, Mock.class);
            }

            final Map<Field, Object> fieldValues = mockFramework.getValues(testFields.toArray(new Field[testFields.size()]));
            if (fieldValues.containsKey(unitField)) {
                throw new IncompatibleAnnotationException(Unit.class, unitField.getType());
            }

            container.createTest(c, fieldValues);

            for (Field field : fieldValues.keySet()) {
                field.setAccessible(true);
                if (field.get(context.getTest().getTarget()) == null) {
                    field.set(context.getTest().getTarget(), fieldValues.get(field));
                }
            }
        } catch (Exception e) {
            throw new TestPluginException("AtUnit configuration error: " + e.getMessage(), e);
        }
    }

    //// TAKEN AS IS FROM AtUnit.java ////

    private Set<Field> getFields(Class<?> c) {
        Set<Field> fields = Sets.newHashSet(c.getDeclaredFields());
        while ((c = c.getSuperclass()) != null) {
            for (Field f : c.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())
                        && !Modifier.isPrivate(f.getModifiers())
                        ) {
                    fields.add(f);
                }
            }
        }
        return fields;
    }

    private Field getUnitField(Set<Field> fields) throws NoUnitException, TooManyUnitsException {
        Field unitField = null;
        for (Field field : fields) {
            for (Annotation anno : field.getAnnotations()) {
                if (Unit.class.isAssignableFrom(anno.annotationType())) {
                    if (unitField != null) throw new TooManyUnitsException("Already had field " + unitField + " when I found field " + field);
                    unitField = field;
                }
            }
        }
        if (unitField == null) throw new NoUnitException();
        return unitField;
    }


    private Container getContainerFor(Class<?> testClass) throws Exception {
        Class<? extends Container> containerClass = NoContainer.class;
        atunit.Container containerAnno = testClass.getAnnotation(atunit.Container.class);
        if (containerAnno != null)
            throw new IllegalStateException("Mycila Integration Framework does not support default @Container annotation. Use instead @ContainerClass with Mycila custom implementations in com.mycila.testing.plugin.atunit.container");
        atunit.ContainerClass containerClassAnno = testClass.getAnnotation(atunit.ContainerClass.class);
        if (containerClassAnno != null) {
            containerClass = containerClassAnno.value();
        }
        return containerClass.newInstance();
    }

    private MockFramework getMockFrameworkFor(Class<?> testClass) throws Exception {
        Class<? extends MockFramework> mockFrameworkClass = NoMockFramework.class;
        atunit.MockFramework mockFrameworkAnno = testClass.getAnnotation(atunit.MockFramework.class);
        if (mockFrameworkAnno != null)
            throw new IllegalStateException("Mycila Integration Framework does not support default @MockFramework annotation. Use instead @MockFrameworkClass with Mycila custom implementations in com.mycila.testing.plugin.atunit.mocker");
        atunit.MockFrameworkClass mockFrameworkClassAnno = testClass.getAnnotation(atunit.MockFrameworkClass.class);
        if (mockFrameworkClassAnno != null) {
            mockFrameworkClass = mockFrameworkClassAnno.value();
        }
        return mockFrameworkClass.newInstance();
    }

}