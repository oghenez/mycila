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
package com.mycila.testing.core;

import com.mycila.testing.core.api.TestContext;
import com.mycila.testing.core.api.TestExecution;
import com.mycila.testing.core.introspect.IntrospectorTest;
import com.mycila.testing.core.plugin.DefaultTestPlugin;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MyPlugin extends DefaultTestPlugin {

    @IntrospectorTest.Annot
    private Integer field3;

    public static boolean prepared = false;
    public static List<String> befores = new ArrayList<String>();
    public static List<String> afters = new ArrayList<String>();
    public static List<String> ends = new ArrayList<String>();

    @Override
    public void prepareTestInstance(TestContext context) {
        prepared = true;
    }

    @Override
    public void beforeTest(TestExecution testExecution) throws Exception {
        befores.add(testExecution.method().getName());
        if (testExecution.method().getName().equals("test_fireEvents_Exceptions1")) {
            throw new IllegalArgumentException("Hello 1");
        } else if (testExecution.method().getName().equals("test_skip")) {
            assertFalse(testExecution.mustSkip());
            testExecution.setSkip(true);
        }
    }

    @Override
    public void afterTest(TestExecution testExecution) throws Exception {
        afters.add(testExecution.method().getName());
        if (testExecution.method().getName().equals("test_fireEvents_Exceptions2")) {
            throw new IllegalArgumentException("Hello 2");
        }
    }

    @Override
    public void afterClass(TestContext context) throws Exception {
        ends.add(context.introspector().testClass().getName());
    }
}
