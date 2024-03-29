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

import com.mycila.testing.core.TestSetup;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.LastWordNamingScheme;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Usage2Test {

    @MockContext
    Mockery mockery;

    @Mock
    Service service;

    @BeforeClass
    public void setup() {
        TestSetup.setup(this);
    }

    @Test
    public void test_go() {
        mockery.checking(new Expectations() {{
            one(service).go();
            will(returnValue("Hello world !"));
        }});
        assertEquals(service.go(), "Hello world !");
        mockery.assertIsSatisfied();
    }

    @MockContextProvider
    Mockery buildMockery() {
        Mockery m = new Mockery();
        m.setImposteriser(ClassImposteriser.INSTANCE);
        m.setNamingScheme(LastWordNamingScheme.INSTANCE);
        m.setExpectationErrorTranslator(null);
        return m;
    }
}