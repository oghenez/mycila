/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.jms.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mycila.jms.JMSClient;
import com.mycila.jms.JMSListener;
import com.mycila.jms.SimpleClient;
import com.mycila.jms.annotation.Destination;
import com.mycila.jms.annotation.Destinations;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mathieu Carbou
 */
public final class JMSClientModule extends AbstractModule {

    private static final Logger LOGGER = Logger.getLogger(JMSClientModule.class.getName());

    private final String clientName;

    public JMSClientModule() {
        this(System.getProperty("jms.client.name", "") + "-" + UUID.randomUUID().toString());
    }

    public JMSClientModule(String clientName) {
        this.clientName = clientName;
    }

    @Override
    protected void configure() {
        bind(Init.class);
        requireBinding(ConnectionFactory.class);
        bindListener(new AbstractMatcher<TypeLiteral<?>>() {
                         @Override
                         public boolean matches(TypeLiteral<?> type) {
                             return JMSListener.class.isAssignableFrom(type.getRawType()) && (type.getRawType().isAnnotationPresent(Destination.class) || type.getRawType().isAnnotationPresent(Destinations.class));
                         }
                     }, new TypeListener() {
                         @Override
                         public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                             final Provider<JMSClient> client = encounter.getProvider(JMSClient.class);
                             encounter.register(new InjectionListener<I>() {
                                 @Override
                                 public void afterInjection(I injectee) {
                                     List<Destination> destinations = new LinkedList<Destination>();
                                     if (injectee.getClass().isAnnotationPresent(Destinations.class)) {
                                         destinations.addAll(Arrays.asList(injectee.getClass().getAnnotation(Destinations.class).value()));
                                     }
                                     if (injectee.getClass().isAnnotationPresent(Destination.class)) {
                                         destinations.add(injectee.getClass().getAnnotation(Destination.class));
                                     }
                                     for (Destination destination : destinations) {
                                         if (LOGGER.isLoggable(Level.INFO)) {
                                             LOGGER.info("Registering JMS subscriber " + injectee.getClass().getName() + " to " + destination.value());
                                         }
                                         client.get().subscribe(
                                             destination.value(),
                                             destination.selector().length() > 0 ? destination.selector() : null,
                                             (JMSListener) injectee);
                                     }
                                 }
                             });
                         }
                     }
        );
    }

    @Provides
    @Singleton
    JMSClient jmsClient(ConnectionFactory connectionFactory) {
        SimpleClient client = new SimpleClient(
            connectionFactory,
            clientName,
            new ExceptionListener() {
                @Override
                public void onException(JMSException exception) {
                    LOGGER.log(Level.SEVERE, "Exception in JMS Client ID " + clientName + ": " + exception.getMessage() + ". Code: " + exception.getErrorCode(), exception);
                }
            });
        LOGGER.info("Starting JMS connection...");
        client.start();
        return client;
    }

    @Singleton
    static class Init {

        @Inject
        JMSClient jmsClient;

        @PreDestroy
        void close() {
            LOGGER.info("Closing JMS connection...");
            jmsClient.stop();
            LOGGER.info("Closed !");
        }

    }

}
