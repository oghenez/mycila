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
import atunit.example.subjects.Logger;
import atunit.example.subjects.User;
import atunit.example.subjects.UserDao;
import atunit.example.subjects.UserManagerImpl;
import atunit.spring.Bean;
import com.mycila.testing.core.TestSetup;
import com.mycila.testing.plugin.atunit.container.SpringContainer;
import com.mycila.testing.plugin.atunit.mocker.EasyMockFramework;
import static org.easymock.EasyMock.*;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@ContainerClass(SpringContainer.class)
@MockFrameworkClass(EasyMockFramework.class)
public class ExampleSpringEasyMockTest {

    @Bean
    @Unit
    UserManagerImpl manager;

    @Bean("fred")
    User fred;

    @Bean("userDao")
    @Mock
    UserDao dao;
    @Bean("log")
    @Stub
    Logger log;

    @BeforeClass
    public void setup() {
        TestSetup.staticDefaultSetup().prepare(this);
    }
    
    @Test
    public void testGetUser() {
        expect(dao.load(1)).andReturn(fred);
        replay(dao);
        assertSame(fred, manager.getUser(1));
        verify(dao);
    }
}