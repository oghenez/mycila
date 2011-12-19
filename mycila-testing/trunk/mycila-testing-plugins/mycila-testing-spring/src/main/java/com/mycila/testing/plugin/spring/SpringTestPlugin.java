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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.springframework.util.ClassUtils;

import com.mycila.testing.core.api.TestContext;
import com.mycila.testing.core.api.TestExecution;
import com.mycila.testing.core.plugin.DefaultTestPlugin;

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
            if ( isSpring31() ) {
                //Changed in http://www.swiftmind.com/de/2011/06/22/spring-3-1-m2-testing-with-configuration-classes-and-profiles/ "ApplicationContext Caching" - so force a refresh here
                ctx.markApplicationContextDirty();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void afterTest(TestExecution testExecution) throws Exception {
        TestContextManager manager = testExecution.context().attributes().get(TESTCONTEXTMANAGER);
        manager.afterTestMethod(
                testExecution.context().introspector().instance(),
                testExecution.method(),
                testExecution.throwable());
    }

    @Override
    public void beforeTest(TestExecution testExecution) throws Exception {
        TestContextManager manager = testExecution.context().attributes().get(TESTCONTEXTMANAGER);
        manager.beforeTestMethod(
                testExecution.context().introspector().instance(),
                testExecution.method());
    }
    
    private boolean isSpring31() {
        return ClassUtils.isPresent( "org.springframework.test.context.MergedContextConfiguration", getClass().getClassLoader() );
    }
    
    private void setupContextLoader(org.springframework.test.context.TestContext ctx, MycilaContextLoader loader) throws Exception {
        if ( !isSpring31() ) {
            Field locations = ctx.getClass().getDeclaredField("locations");
            locations.setAccessible(true);
            locations.set(ctx, loader.contextLocations());

            Field contextLoader = ctx.getClass().getDeclaredField("contextLoader");
            contextLoader.setAccessible(true);
            contextLoader.set(ctx, loader);
            
            Field contextCache = ctx.getClass().getDeclaredField("contextCache");
            contextCache.setAccessible(true);
            Constructor<?> ctor = Class.forName("org.springframework.test.context.ContextCache").getDeclaredConstructor();
            ctor.setAccessible(true);
            contextCache.set(ctx, ctor.newInstance());
        }
        else {
            Field mergedContextConfigurationField = ctx.getClass().getDeclaredField("mergedContextConfiguration");
            mergedContextConfigurationField.setAccessible(true);
            Object mergedContextConfiguration = mergedContextConfigurationField.get(ctx);
            Field locations = mergedContextConfiguration.getClass().getDeclaredField("locations");
            locations.setAccessible(true);
            locations.set(mergedContextConfiguration, loader.contextLocations());
            
            Field contextLoaderField = mergedContextConfiguration.getClass().getDeclaredField("contextLoader");
            contextLoaderField.setAccessible(true);
            contextLoaderField.set(mergedContextConfiguration, loader);
        }
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
