/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.inject.injector;

import com.google.inject.Injector;
import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotatedMemberTypeListener<A extends Annotation> implements TypeListener {
    @Inject
    private Injector injector;
    private final Class<A> annotationType;
    private final Class<? extends KeyProvider<A>> providerClass;

    public AnnotatedMemberTypeListener(Class<A> annotationType, Class<? extends KeyProvider<A>> providerClass) {
        this.annotationType = annotationType;
        this.providerClass = providerClass;
    }

    @Override
    public <I> void hear(TypeLiteral<I> injectableType, TypeEncounter<I> encounter) {
        // find injectable fields
        final List<AnnotatedMember<Field, A>> fields = Members.getAnnotatedFields(injectableType, annotationType);
        // find injectable members
        //TODO: final List<Method> methods = Methods.listAll(type, Matchers.annotatedWith(Resource.class));

        if (!fields.isEmpty() /*|| !methods.isEmpty()*/) {
            final Provider<? extends KeyProvider<A>> provider = encounter.getProvider(providerClass);
            encounter.register(new MembersInjector<I>() {
                @Override
                public void injectMembers(I instance) {
                    KeyProvider<A> keyProvider = provider.get();
                    for (AnnotatedMember<Field, A> member : fields) {
                        Field field = member.getMember();
                        if (!field.isAccessible())
                            field.setAccessible(true);
                        try {
                            field.set(instance, injector.getProvider(keyProvider.getKey(member)));
                        } catch (IllegalAccessException e) {
                            throw new ProvisionException("Unable to inject field " + field + ". Reason: " + e.getMessage(), e);
                        }
                    }

                }
            });
        }

        /*Class<?> type = injectableType.getRawType();
        while (type != Object.class && type != null) {
            Method[] methods = type.getDeclaredMethods();
            for (final Method method : methods) {
                MethodKey key = new MethodKey(method, method.getParameterTypes());
                if (processedMethods.get(key) == null) {
                    processedMethods.put(key, method);
                    bindAnnotationInjectionToMember(encounter, startType, method);
                }
            }

            Class<?> supertype = type.getSuperclass();
            if (supertype == Object.class) {
                break;
            }
            startType = startType.getSupertype(supertype);
        }*/

    }
}
