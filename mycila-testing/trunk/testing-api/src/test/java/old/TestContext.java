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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TestContext {

    private static final Map<Class<?>, TestContext> contexts = new HashMap<Class<?>, TestContext>();

    /*private final Injector injector;
    private final Mockery mockery;

    @SuppressWarnings({"unchecked"})
    private TestContext(Object testInstance) {
        GuicePlugin guicePlugin = new GuicePlugin(testInstance);
        JMockPlugin jMockPlugin = new JMockPlugin(testInstance);
        mockery = jMockPlugin.buildMockery();
        injector = Guice.createInjector(guicePlugin.stage(), guicePlugin.buildModules());
        injector.injectMembers(testInstance);
    }

    public Injector injector() {
        return injector;
    }

    public Mockery mockery() {
        return mockery;
    }*/

    public static TestContext setup(Object testInstance) {
        //TestContext ctx = new TestContext(testInstance);
        //contexts.put(testInstance.getClass(), ctx);
        //return ctx;
        return null;
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    public static TestContext get() {
        try {
            return get(Class.forName(new Throwable().getStackTrace()[1].getClassName()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static TestContext get(Object testInstance) {
        return get(testInstance.getClass());
    }

    public static TestContext get(Class<?> testClass) {
        TestContext testContext = contexts.get(testClass);
        if (testContext == null) {
            throw new IllegalArgumentException("No TestContext registered for test: " + testClass.getName());
        }
        return testContext;
    }
}
