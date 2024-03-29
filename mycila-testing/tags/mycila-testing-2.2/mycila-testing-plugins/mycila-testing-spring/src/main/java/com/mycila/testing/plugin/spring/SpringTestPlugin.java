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
import com.mycila.testing.core.plugin.DefaultTestPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SpringTestPlugin extends DefaultTestPlugin {

    public static final String TESTCONTEXTMANAGER = "org.springframework.test.context.TestContextManager";
    public static final String TESTCONTEXT = "org.springframework.test.context.TestContext";
    public static final String APPLICATIONCONTEXT = "org.springframework.context.ApplicationContext";

    @Override
    public void prepareTestInstance(TestContext context) {
        try {
            final TestContextManager manager = new TestContextManager(context.introspector().testClass());
            final org.springframework.test.context.TestContext ctx = manager.testContext();
            context.attributes().set(TESTCONTEXTMANAGER, manager);
            context.attributes().set(TESTCONTEXT, ctx);
            setupContextLoader(ctx, new MycilaContextLoader(context));
            manager.prepareTestInstance(context.introspector().instance());
            context.attributes().set(APPLICATIONCONTEXT, manager.testContext().getApplicationContext());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void setupContextLoader(org.springframework.test.context.TestContext ctx, MycilaContextLoader loader) throws Exception {
        Field locations = ctx.getClass().getDeclaredField("locations");
        locations.setAccessible(true);
        locations.set(ctx, loader.contextLocations());
        Field contextLoader = ctx.getClass().getDeclaredField("contextLoader");
        contextLoader.setAccessible(true);
        contextLoader.set(ctx, loader);
        Field contextCache = ctx.getClass().getDeclaredField("contextCache");
        contextCache.setAccessible(true);
        Constructor ctor = Class.forName("org.springframework.test.context.ContextCache").getDeclaredConstructor();
        ctor.setAccessible(true);
        contextCache.set(ctx, ctor.newInstance());
    }

    private static class TestContextManager extends org.springframework.test.context.TestContextManager {
        public TestContextManager(Class<?> testClass) {
            super(testClass);
        }

        public org.springframework.test.context.TestContext testContext() {
            return super.getTestContext();
        }
    }
}
