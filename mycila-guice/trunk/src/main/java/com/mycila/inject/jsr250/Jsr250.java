package com.mycila.inject.jsr250;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.ProvisionException;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.BindingScopingVisitor;
import com.mycila.inject.scope.MappedScope;
import com.mycila.inject.util.Methods;

import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Jsr250 {
    private Jsr250() {
    }

    private static final BindingScopingVisitor<Boolean> DEFAULT_VISITOR = destroyableVisitor(Collections.<Scope>emptySet(), Collections.<Class<? extends Annotation>>emptySet());

    static BindingScopingVisitor<Boolean> destroyableVisitor(final Collection<Scope> additionalScopes, final Collection<Class<? extends Annotation>> additionalScopeAnnotations) {
        return new BindingScopingVisitor<Boolean>() {
            @Override
            public Boolean visitEagerSingleton() {
                return true;
            }

            @Override
            public Boolean visitScope(Scope scope) {
                return scope.equals(Scopes.SINGLETON)
                        || scope.getClass().isAnnotationPresent(Jsr250Destroyable.class)
                        || additionalScopes.contains(scope)
                        || scope instanceof MappedScope && ((MappedScope) scope).isSingleton();
            }

            @Override
            public Boolean visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
                return scopeAnnotation.equals(Singleton.class)
                        || scopeAnnotation.isAnnotationPresent(Jsr250Destroyable.class)
                        || additionalScopeAnnotations.contains(scopeAnnotation);
            }

            @Override
            public Boolean visitNoScoping() {
                return false;
            }
        };
    }

    static Iterable<Jsr250Element> destroyables(Injector injector, BindingScopingVisitor<Boolean> visitor) {
        List<Jsr250Element> elements = new LinkedList<Jsr250Element>();
        for (Binding<?> binding : injector.getAllBindings().values()) {
            elements.add(destroyable(binding, visitor));
        }
        for (Scope scope : injector.getScopeBindings().values()) {
            elements.add(destroyable(scope));
        }
        return elements;
    }

    static Jsr250Element destroyable(final Binding<?> binding) {
        return destroyable(binding, DEFAULT_VISITOR);
    }

    static Jsr250Element destroyable(final Binding<?> binding, final BindingScopingVisitor<Boolean> visitor) {
        return new Jsr250Element() {
            @Override
            public void preDestroy() {
                Boolean res = binding.acceptScopingVisitor(visitor);
                if (res != null && res) {
                    Object o = binding.getProvider().get();
                    if (o != null)
                        Jsr250.invoke(o, javax.annotation.PreDestroy.class);
                }
            }
        };
    }

    static Jsr250Element destroyable(final Scope scope) {
        return new Jsr250Element() {
            @Override
            public void preDestroy() {
                if (scope != null)
                    Jsr250.invoke(scope, javax.annotation.PreDestroy.class);
            }
        };
    }

    public static boolean hasJSR250Module(Injector injector) {
        return injector.getBindings().containsKey(Key.get(Jsr250Destroyer.class));
    }

    public static void preDestroy(Object injectee) {
        invoke(injectee, javax.annotation.PreDestroy.class);
    }

    static void invoke(Object injectee, Class<? extends Annotation> annotationClass) {
        Class<?> c = injectee.getClass();
        if (c.getName().contains("$$"))
            c = c.getSuperclass();
        List<Method> methods = Methods.listAll(c, Methods.METHOD_WITHOUT_PARAMETER.and(Matchers.annotatedWith(annotationClass)));
        for (Method method : methods) {
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
}
