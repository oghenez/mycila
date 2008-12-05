package com.mycila.testing.plugin.atunit;

import atunit.*;
import atunit.example.subjects.GuiceUserManager;
import atunit.example.subjects.Logger;
import atunit.example.subjects.User;
import atunit.example.subjects.UserDao;
import com.google.inject.Inject;
import com.mycila.testing.core.TestSetup;
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
        TestSetup.setup(this);
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