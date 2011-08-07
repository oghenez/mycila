package com.mycila.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Stage;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.inject.jsr250.Jsr250Injector;
import org.junit.Test;

public class ExplicitBindingTest {
    @Test
    public void testExplicit() {
        Jsr250Injector injector = Jsr250.createInjector(Stage.PRODUCTION, new AbstractModule() {
            @Override
            protected void configure() {
                binder().requireExplicitBindings();
            }
        });
    }
}
