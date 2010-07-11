package com.mycila.plugin.scope;

import com.mycila.plugin.Invokable;
import com.mycila.plugin.Provider;
import com.mycila.plugin.scope.defaults.ExpiringSingleton;
import com.mycila.plugin.scope.defaults.None;
import com.mycila.plugin.scope.defaults.Singleton;
import com.mycila.plugin.scope.defaults.WeakSingleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ScopeTest {

    @Test
    public void test_none() throws Exception {
        ScopeContext<String> mock = mock(ScopeContext.class);
        Invokable invokable = mock(Invokable.class);
        when(mock.getInvokable()).thenReturn(invokable);
        when(invokable.invoke()).thenAnswer(new Answer<String>() {
            int count = 0;

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "" + count++;
            }
        });

        Provider<String> provider = ScopeProviders.build(None.class, mock);

        assertEquals("0", provider.get());
        assertEquals("1", provider.get());
    }

    @Test
    public void test_singleton() throws Exception {
        ScopeContext<String> mock = mock(ScopeContext.class);
        Invokable invokable = mock(Invokable.class);
        when(mock.getInvokable()).thenReturn(invokable);
        when(invokable.invoke()).thenAnswer(new Answer<String>() {
            int count = 0;

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "" + count++;
            }
        });

        Provider<String> provider = ScopeProviders.build(Singleton.class, mock);

        assertEquals("0", provider.get());
        assertEquals("0", provider.get());
    }

    @Test
    public void test_weak() throws Exception {
        ScopeContext<Object> mock = mock(ScopeContext.class);
        Invokable invokable = mock(Invokable.class);
        when(mock.getInvokable()).thenReturn(invokable);
        when(invokable.invoke()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new Object();
            }
        });

        Provider<Object> provider = ScopeProviders.build(WeakSingleton.class, mock);

        Object o = provider.get();
        int hash = o.hashCode();
        assertNotNull(o);

        System.gc();
        System.gc();

        assertSame(o, provider.get());

        o = provider.get();
        System.gc();
        System.gc();

        assertNotSame(hash, o.hashCode());
    }

    @Test
    public void test_expire() throws Exception {
        ScopeContext<Object> mock = mock(ScopeContext.class);
        when(mock.getParameter("duration")).thenReturn("500");
        Invokable invokable = mock(Invokable.class);
        when(mock.getInvokable()).thenReturn(invokable);
        when(invokable.invoke()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new Object();
            }
        });

        Provider<Object> provider = ScopeProviders.build(ExpiringSingleton.class, mock);

        Object o = provider.get();
        assertNotNull(o);
        assertSame(o, provider.get());

        Thread.sleep(600);

        assertNotSame(o, provider.get());
    }

    @Test
    public void test_expire_param_fail() throws Exception {
        ScopeContext<Object> mock = mock(ScopeContext.class);
        when(mock.toString()).thenReturn("a context");
        MissingScopeParameterException exception = new MissingScopeParameterException(mock, "duration");
        when(mock.getParameter("duration")).thenThrow(exception);
        try {
            ScopeProviders.build(ExpiringSingleton.class, mock);
            fail();
        } catch (Exception e) {
            assertSame(exception, e);
            assertEquals("Scope parameter 'duration' is missing at: a context", e.getMessage());
        }
    }

    @Test
    public void test_creation_fail() throws Exception {
        ScopeContext<Object> mock = mock(ScopeContext.class);
        try {
            ScopeProviders.build(Error1.class, mock);
            fail();
        } catch (Exception e) {
            assertSame(ScopeInstanciationException.class, e.getClass());
            assertEquals("Unable to instanciate scope com.mycila.plugin.scope.Error1: com.mycila.plugin.scope.Error1.<init>()", e.getMessage());
        }
        try {
            ScopeProviders.build(Error2.class, mock);
            fail();
        } catch (Exception e) {
            assertSame(ScopeInstanciationException.class, e.getClass());
            assertEquals("Unable to instanciate scope com.mycila.plugin.scope.Error2: yo", e.getMessage());
        }
    }
}
