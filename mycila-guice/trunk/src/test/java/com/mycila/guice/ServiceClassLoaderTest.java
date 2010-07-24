package com.mycila.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ServiceClassLoaderTest {

    @Test
    public void test() throws Exception {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(new TypeLiteral<Iterable<Class<Serv>>>(){}).toInstance(ServiceClassLoader.load(Serv.class));
            }
        });
        int count = 0;
        for (Class<Serv> s : injector.getInstance(Key.get(new TypeLiteral<Iterable<Class<Serv>>>() {
        }))) count++;
        assertEquals(2, count);
    }

}
