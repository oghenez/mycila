package com.mycila.guice;

import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mycila.guice.annotation.PostInject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class PostInjectionListener implements TypeListener {
    @Override
    public <I> void hear(final TypeLiteral<I> type, TypeEncounter<I> encounter) {
        encounter.register(new InjectionListener<I>() {
            @Override
            public void afterInjection(I injectee) {
                for (Method method : Methods.listAll(
                        type.getRawType(),
                        Methods.METHOD_WITHOUT_PARAMETER.and(Matchers.annotatedWith(PostInject.class)))) {
                    if (!method.isAccessible())
                        method.setAccessible(true);
                    try {
                        method.invoke(injectee);
                    } catch (IllegalAccessException e) {
                        throw new ProvisionException(e.getMessage(), e);
                    } catch (InvocationTargetException e) {
                        throw new ProvisionException(e.getTargetException().getMessage(), e.getTargetException());
                    }
                }
            }
        });
    }
}
