/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

import org.guiceyfruit.support.Closer;

import javax.annotation.PreDestroy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Supports the {@link javax.annotation.PreDestroy} annotation lifecycle from JSR250.
 * <p/>
 * To install this closer you need to register the {@link org.guiceyfruit.jsr250.Jsr250Module} in your injector.
 *
 * @author james.strachan@gmail.com (James Strachan)
 * @version $Revision: 1.1 $
 */
class PreDestroyCloser implements Closer {
    private AnnotatedMethodCache methodCache = new AnnotatedMethodCache(PreDestroy.class);
    public void close(Object object) throws Throwable {
        Class<?> type = object.getClass();
        Method method = methodCache.getMethod(type);
        if (method != null) {
            try {
                method.setAccessible(true);
                method.invoke(object);
            }
            catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}