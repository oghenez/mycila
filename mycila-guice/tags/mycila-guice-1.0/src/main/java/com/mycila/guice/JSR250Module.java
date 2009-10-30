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
package com.mycila.guice;

import org.guiceyfruit.jsr250.ResourceMemberProvider;
import org.guiceyfruit.support.GuiceyFruitModule;
import org.guiceyfruit.support.MethodHandler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class JSR250Module extends GuiceyFruitModule {
    protected void configure() {
        super.configure();
        bindAnnotationInjector(Resource.class, ResourceMemberProvider.class);
        bindMethodHandler(PostConstruct.class, new MethodHandler() {
            public void afterInjection(Object injectee, Annotation annotation, Method method)
                    throws InvocationTargetException, IllegalAccessException {
                method.setAccessible(true);
                method.invoke(injectee);
            }
        });
        bind(PreDestroyCloser.class);
    }
}