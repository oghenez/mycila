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

package com.mycila.inject.web;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.inject.util.Modules;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.inject.jsr250.Jsr250Injector;
import com.mycila.inject.service.ServiceModules;

import javax.servlet.ServletContextEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 * @date 2013-01-30
 */
public class GuiceListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Jsr250.createInjector(Stage.PRODUCTION, Modules.override(ServiceModules.loadFromClasspath(Module.class), new ServletModule() {
            @Override
            protected void configureServlets() {
                filter("/*").through(HttpContextFilter.class);
            }
        }).with(HttpContext.MODULE));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Jsr250Injector injector = (Jsr250Injector) servletContextEvent.getServletContext().getAttribute(Injector.class.getName());
        if (injector != null) {
            injector.destroy();
        }
        super.contextDestroyed(servletContextEvent);
        // fixes and hugly Guice 3 mempry leak
        // http://stackoverflow.com/questions/8842256/guice-3-0-tomcat-7-0-classloader-memory-leak
        try {
            final Class<?> queueHolderClass = Class.forName("com.google.inject.internal.util.$MapMaker$QueueHolder");
            final Field queueField = queueHolderClass.getDeclaredField("queue");
            // make MapMaker.QueueHolder.queue accessible
            queueField.setAccessible(true);
            // remove the final modifier from MapMaker.QueueHolder.queue
            final Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(queueField, queueField.getModifiers() & ~Modifier.FINAL);
            // set it to null
            queueField.set(null, null);
        } catch (Exception ignored) {
        }
    }
}
