package com.mycila.testing.plugin.guice;

import com.google.inject.AbstractModule;
import static com.google.inject.name.Names.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class AModule extends AbstractModule {
    protected void configure() {
        bind(Service.class).annotatedWith(named("service1")).toInstance(new Service() {
            public String go() {
                return "go1";
            }
        });
    }
}
