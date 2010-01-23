package service;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mycila.guice.loader.ServiceLoaderModule;
import com.mycila.guice.loader.ServiceLoaderProvider;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ServiceTest {

    @Inject
    Set<Service> services;

    @Test
    public void test_array_binding() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Service[].class).toProvider(ServiceLoaderProvider.of(Service.class));
            }
        });
        Service[] services = injector.getInstance(Service[].class);
        assertEquals(services.length, 2);
        assertNotNull(services[0].get());
        assertNotNull(services[1].get());
    }

    @Test
    public void test_multibindings() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                install(ServiceLoaderModule.of(Service.class));
            }
        });

        injector.injectMembers(this);
        assertEquals(services.size(), 2);
        assertNotNull(new ArrayList<Service>(services).get(0).get());
        assertNotNull(new ArrayList<Service>(services).get(1).get());
    }

}
