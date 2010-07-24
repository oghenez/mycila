package com.mycila.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mycila.guice.annotation.PostInject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class PostInjectionListenerTest {

    @Test
    public void test() throws Exception {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                MycilaGuice.bindPostInjectListener(binder());
            }
        });
        assertEquals("[1, 2]", injector.getInstance(A.class).calls.toString());
    }

    static class A {
        List<Integer> calls = new LinkedList<Integer>();

        @Inject
        void methos(B b) {
            calls.add(1);
        }

        @PostInject
        void init() {
            calls.add(2);
        }
    }

    static class B {
    }
}
