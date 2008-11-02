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

package old;

import com.mycila.testing.plugin.jmock.Mock;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JMockPlugin {

    private final Object testInstance;

    public JMockPlugin(Object testInstance) {
        this.testInstance = testInstance;
    }

    public Mockery buildMockery() {
        Class<?> testClass = testInstance.getClass();
        List<Field> fields = ClassUtils.getFields(testClass, Mock.class);
        boolean withClassImposteriser = false;

        // check if the imposteriser should use cglib
        for (Field field : fields) {
            if (!field.getType().isInterface()) {
                withClassImposteriser = true;
                break;
            }
        }

        // create mockery and mock
        Mockery mockery = createMockery(withClassImposteriser);
        for (Field field : fields) {
            try {
                field.set(testInstance, mockery.mock(field.getType()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return mockery;
    }

    private Mockery createMockery(final boolean withClassImposteriser) {
        return new Mockery() {
            {
                if (withClassImposteriser) {
                    setImposteriser(ClassImposteriser.INSTANCE);
                }
            }
        };
    }

}