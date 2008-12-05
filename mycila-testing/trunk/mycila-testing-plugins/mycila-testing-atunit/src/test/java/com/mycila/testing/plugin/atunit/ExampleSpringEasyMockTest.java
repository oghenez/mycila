package com.mycila.testing.plugin.atunit;

import atunit.*;
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
        TestSetup.setup(this);
    }
    
    @Test
    public void testGetUser() {
        expect(dao.load(1)).andReturn(fred);
        replay(dao);
        assertSame(fred, manager.getUser(1));
        verify(dao);
    }
}