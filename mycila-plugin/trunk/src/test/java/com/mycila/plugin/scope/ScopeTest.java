package com.mycila.plugin.scope;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
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
        when(mock.invoke()).thenAnswer(new Answer<String>() {
            int count = 0;

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "" + count++;
            }
        });

        None<String> provider = new None<String>();
        provider.init(mock);

        assertEquals("0", provider.get());
        assertEquals("1", provider.get());
    }

    @Test
    public void test_singleton() throws Exception {
        ScopeContext<String> mock = mock(ScopeContext.class);
        when(mock.invoke()).thenAnswer(new Answer<String>() {
            int count = 0;

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "" + count++;
            }
        });

        Singleton<String> provider = new Singleton<String>();
        provider.init(mock);

        assertEquals("0", provider.get());
        assertEquals("0", provider.get());
    }

    @Test
    public void test_weak() throws Exception {
        ScopeContext<Object> mock = mock(ScopeContext.class);
        when(mock.invoke()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new Object();
            }
        });

        WeakSingleton<Object> provider = new WeakSingleton<Object>();
        provider.init(mock);

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
        when(mock.invoke()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new Object();
            }
        });

        ExpiringSingleton<Object> provider = new ExpiringSingleton<Object>();
        provider.init(mock);

        Object o = provider.get();
        assertNotNull(o);
        assertSame(o, provider.get());

        Thread.sleep(600);

        assertNotSame(o, provider.get());
    }
}
