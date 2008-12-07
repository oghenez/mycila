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

import com.mycila.testing.core.TestSetup;
import static org.easymock.classextension.EasyMock.*;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Usage1Test {

    @Mock
    Service service1;

    @Mock(Mock.Type.NICE)
    Service service2;

    @Mock(Mock.Type.STRICT)
    Service service3;

    @BeforeClass
    public void setup() {
        TestSetup.setup(this);
    }

    @Test
    public void test_mock_standard() {
        expect(service1.go()).andReturn("Hello World !");
        replay(service1);
        assertEquals(service1.go(), "Hello World !");
        verify(service1);
    }

    @Test
    public void test_mock_strict_ok() {
        resetToStrict(service3);
        expect(service3.go()).andReturn("Hello World 1 !");
        expect(service3.go2()).andReturn("Hello World 2 !");
        replay(service3);
        assertEquals(service3.go(), "Hello World 1 !");
        assertEquals(service3.go2(), "Hello World 2 !");
        verify(service3);
    }

    @Test(expectedExceptions = AssertionError.class)
    public void test_mock_strict_bad() {
        resetToStrict(service3);
        expect(service3.go()).andReturn("Hello World 1 !");
        expect(service3.go2()).andReturn("Hello World 2 !");
        replay(service3);
        assertEquals(service3.go2(), "Hello World 2 !");
        assertEquals(service3.go(), "Hello World 1 !");
        verify(service3);
    }

    @Test
    public void test_mock_nice() {
        replay(service2);
        assertEquals(service2.go(), null);
        verify(service2);
    }
}