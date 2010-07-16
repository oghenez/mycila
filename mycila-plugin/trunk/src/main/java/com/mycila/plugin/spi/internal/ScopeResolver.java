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

package com.mycila.plugin.spi.internal;

import com.mycila.plugin.Scope;
import com.mycila.plugin.annotation.ScopeAnnotation;
import com.mycila.plugin.spi.invoke.Invokables;
import com.mycila.plugin.spi.invoke.InvokeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ScopeResolver {

    private final ConcurrentMap<Class<?>, Scope> cache = new ConcurrentHashMap<Class<?>, Scope>();
    private final Annotation defaultScope;

    public ScopeResolver(Annotation defaultScope) {
        this.defaultScope = defaultScope;
        if (!defaultScope.annotationType().isAnnotationPresent(ScopeAnnotation.class))
            throw new IllegalArgumentException("Invalid scope annotation " + defaultScope.annotationType().getName() + " : annotation must be annotated by @ScopeAnnotation");
    }

    private Scope load(Class<? extends Scope> scopeClass) throws ScopeInstanciationException {
        try {
            Scope scope = cache.get(scopeClass);
            if (scope == null)
                cache.putIfAbsent(scopeClass, scope = Invokables.get(scopeClass.getConstructor()).invoke());
            return scope;
        } catch (InvokeException e) {
            throw new ScopeInstanciationException(scopeClass, e.getCause());
        } catch (NoSuchMethodException e) {
            throw new ScopeInstanciationException(scopeClass, e);
        }
    }

    public ScopeBinding getScopeBinding(AnnotatedElement member) throws TooManyScopeException {
        Annotation found = null;
        for (Annotation annotation : member.getAnnotations())
            if (annotation.annotationType().isAnnotationPresent(ScopeAnnotation.class))
                if (found == null) found = annotation;
                else throw new TooManyScopeException(member);
        final Annotation finalFound = found == null ? defaultScope : found;
        return new ScopeBinding() {
            @Override
            public Scope getScope() {
                return load(finalFound.annotationType().getAnnotation(ScopeAnnotation.class).value());
            }

            @Override
            public Annotation getAnnotation() {
                return finalFound;
            }

            @Override
            public String toString() {
                return finalFound.toString();
            }
        };
    }

}
