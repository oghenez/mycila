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

import atunit.ContainerClass;
import atunit.Mock;
import atunit.MockFrameworkClass;
import atunit.Stub;
import atunit.Unit;
import atunit.example.subjects.GuiceUserManager;
import atunit.example.subjects.Logger;
import atunit.example.subjects.User;
import atunit.example.subjects.UserDao;
import com.google.inject.Inject;
import com.mycila.testing.core.MycilaTesting;
import com.mycila.testing.plugin.atunit.container.GuiceContainer;
import com.mycila.testing.plugin.atunit.mocker.JMockFramework;
import org.jmock.Expectations;
import org.jmock.Mockery;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@ContainerClass(GuiceContainer.class)
@MockFrameworkClass(JMockFramework.class)
public class ExampleGuiceAndJMockTest {

    @Inject
    @Unit
    GuiceUserManager manager;

    @Inject
    User emptyUser;

    Mockery mockery;

    @Mock
    UserDao dao;
    @Stub
    Logger ignoredLogger;

    @BeforeClass
    public void setup() {
        MycilaTesting.from(getClass()).handle(this).prepare();
    }
    
    @Test
    public void testGetUser() {
        mockery.checking(new Expectations() {{
            one(dao).load(with(equal(500)));
            will(returnValue(emptyUser));
        }});
        assertSame(manager.getUser(500), emptyUser);
        mockery.assertIsSatisfied();
    }
}