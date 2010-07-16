package com.mycila.plugin.spi.model;

import com.mycila.plugin.Provider;
import com.mycila.plugin.annotation.BindingAnnotation;
import com.mycila.plugin.spi.TypeLiteral;
import com.mycila.plugin.spi.invoke.InvokableMember;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Binding<T> {

    private final TypeLiteral<T> type;
    private final Collection<Annotation> annotations;

    private Binding(TypeLiteral<T> type, Collection<Annotation> annotations) {
        this.type = type;
        this.annotations = Collections.unmodifiableCollection(annotations);
    }

    public Collection<Annotation> getAnnotations() {
        return annotations;
    }

    public TypeLiteral<T> getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Annotation annotation : annotations)
            sb.append(annotation).append(" ");
        return sb.append(type).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Binding binding = (Binding) o;
        return annotations.equals(binding.annotations) && type.equals(binding.type);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + annotations.hashCode();
        return result;
    }

    public static <T> Binding<T> fromInvokable(InvokableMember<T> invokable) {
        Collection<Annotation> annotations = new LinkedHashSet<Annotation>();
        for (Annotation annotation : invokable.getMember().getAnnotations())
            if (annotation.annotationType().isAnnotationPresent(BindingAnnotation.class))
                annotations.add(annotation);
        return new Binding<T>(invokable.getType(), annotations);
    }

    public static Collection<Binding<?>> fromParameters(Method method) {
        Collection<Binding<?>> bindings = new ArrayList<Binding<?>>(5);
        List<TypeLiteral<?>> types = TypeLiteral.get(method.getDeclaringClass()).getParameterTypes(method);
        Annotation[][] annots = method.getParameterAnnotations();
        for (int i = 0; i < types.size(); i++) {
            // annotations
            Collection<Annotation> annotations = new LinkedHashSet<Annotation>();
            for (Annotation annotation : annots[i])
                if (annotation.annotationType().isAnnotationPresent(BindingAnnotation.class))
                    annotations.add(annotation);
            // type
            if (types.get(i).getRawType() == Provider.class) {
                Type[] args = ((ParameterizedType) types.get(i)).getActualTypeArguments();
                if (args.length == 0)
                    throw new IllegalStateException("Missing type argument for provider at ");
                bindings.add(binding(types.get(i), annotations));
            }
        }
        return Collections.unmodifiableCollection(bindings);
    }

    private static <T> Binding<T> binding(TypeLiteral<T> type, Collection<Annotation> annotations) {
        return new Binding<T>(type, annotations);
    }
}


