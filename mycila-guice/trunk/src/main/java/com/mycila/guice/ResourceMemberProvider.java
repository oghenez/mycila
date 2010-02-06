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

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import org.guiceyfruit.support.AnnotationMemberProviderSupport;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * Injects fields or methods with the results of the {@link javax.annotation.Resource} annotation
 *
 * @version $Revision: 1.1 $
 */
class ResourceMemberProvider extends AnnotationMemberProviderSupport<Resource> {

    private Injector injector;
    private Context context;

    @Inject
    public ResourceMemberProvider(Injector injector) {
        this.injector = injector;
    }

    public Context getContext() {
        return context;
    }

    @Inject(optional = true)
    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isNullParameterAllowed(Resource annotation, Method method, Class<?> parameterType,
                                          int parameterIndex) {
        return false;
    }

    protected Object provide(Resource resource, Member member, TypeLiteral<?> requiredType,
                             Class<?> memberType, Annotation[] annotations) {
        String name = getJndiName(resource, member);
        Binding<?> binding = injector.getBinding(requiredType, name);
        if (binding != null)
            return binding.getProvider().get();
        // NOT JSR250, but even try to get defaut non named bindings if exist
        binding = injector.getBinding(requiredType);
        if (binding != null)
            return binding.getProvider().get();
        try {
            if (context == null)
                context = new InitialContext();
            return context.lookup(name);
        }
        catch (NamingException e) {
            throw new ProvisionException("Failed to find name '" + name + "' in JNDI. Cause: " + e, e);
        }
    }

    /**
     * Deduces the JNDI name from the resource and member according to the JSR 250 rules
     */
    String getJndiName(Resource resource, Member member) {
        String answer = resource.name();
        if (answer == null || answer.length() == 0) {
            answer = member.getName();
        }
        if (answer == null || answer.length() == 0) {
            throw new IllegalArgumentException("No name defined");
        }
        return answer;
    }
}