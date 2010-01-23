/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
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
