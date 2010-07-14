package com.mycila.plugin.scope;

import com.mycila.plugin.annotation.ScopeAnnotation;
import com.mycila.plugin.invoke.Invokables;
import com.mycila.plugin.invoke.InvokeException;
import com.mycila.plugin.scope.annotation.None;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultScopeResolver implements ScopeResolver {

    public static final None DEFAULT = new None() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return None.class;
        }
    };

    private final ConcurrentMap<Class<?>, Scope> cache = new ConcurrentHashMap<Class<?>, Scope>();
    private final Annotation defaultScope;

    public Annotation getDefaultScope() {
        return defaultScope;
    }

    public DefaultScopeResolver() {
        this(DEFAULT);
    }

    public DefaultScopeResolver(Annotation defaultScope) {
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

    @Override
    public ScopeBinding getScopeBinding(AnnotatedElement member) {
        Annotation found = null;
        for (Annotation annotation : member.getAnnotations())
            if (annotation.annotationType ().isAnnotationPresent(ScopeAnnotation.class))
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
                return "@" + finalFound.annotationType().getSimpleName();
            }
        };
    }

}
