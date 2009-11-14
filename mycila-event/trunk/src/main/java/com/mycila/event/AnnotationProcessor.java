package com.mycila.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotationProcessor {

    private final Dispatcher dispatcher;

    public AnnotationProcessor(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void process(Object o) {

    }

    public static List<Method> getAllDeclaredMethods(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<Class<?>>();
        while (clazz != Object.class) {
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }
        LinkedHashMap<MethodSignature, Method> all = new LinkedHashMap<MethodSignature, Method>();
        for (Class<?> c : hierarchy) {
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                MethodSignature signature = MethodSignature.of(method);
                if (!all.containsKey(signature))
                    all.put(signature, method);
            }
        }
        return new LinkedList<Method>(all.values());

    }

    /*private static boolean isValidMethod(InjectableMethod injectableMethod,
                                         Errors errors) {
        boolean result = true;
        if (injectableMethod.jsr330) {
            Method method = injectableMethod.method;
            if (Modifier.isAbstract(method.getModifiers())) {
                errors.cannotInjectAbstractMethod(method);
                result = false;
            }
            if (method.getTypeParameters().length > 0) {
                errors.cannotInjectMethodWithTypeParameters(method);
                result = false;
            }
        }
        return result;
    }

    boolean overrides(ConcreteMethod other) {
        return overrides(other.method);
    }

    boolean overrides(Method other) {
        // See JLS section 8.4.8.1
        int modifiers = other.getModifiers();
        if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers))
            return true;
        if (Modifier.isPrivate(modifiers))
            return false;
        // b must be package-private
        return method.getDeclaringClass().getPackage().equals(other.getDeclaringClass().getPackage());
    }

            @Override
            public List<Method> select() {
                final LinkedList<Method> methods = new LinkedList<Method>(methodFilter.select());
                // first pass to eliminate non overridable methods
                for (Iterator<Method> iterator = methods.iterator(); iterator.hasNext();) {
                    final Method m = iterator.next();
                    final int modifiers = m.getModifiers();
                    if ((modifiers & (FINAL | PRIVATE | STATIC | NATIVE)) != 0) {
                        elements.add(m);
                        iterator.remove();
                    } else if ((modifiers & (INTERFACE | ABSTRACT | VOLATILE)) != 0) {
                        iterator.remove();
                    }
                }
                // second pass to eliminate each remaining overridable methods
                while (!methods.isEmpty()) {
                    // pick a remaining method
                    final Method m1 = methods.poll();
                    boolean overriden = false;
                    // check if it overrides to is overriden against each other ones
                    for (Iterator<Method> iterator = methods.iterator(); iterator.hasNext();) {
                        final Method m2 = iterator.next();
                        if (m1.getName().equals(m2.getName()) && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes())) {
                            // if the signature is the same, we have to check for override
                            final Class<?> c = m1.getDeclaringClass();
                            final Class<?> a = m2.getDeclaringClass();
                            if (a.isAssignableFrom(c)) {
                                // m1 overrides m2 => remove m2
                                iterator.remove();
                            } else if (c.isAssignableFrom(a)) {
                                // if m2 overrides m1 => stop and pick next method
                                overriden = true;
                                break;
                            }
                        }
                    }
                    if (!overriden) {
                        // we removed all methods m1 overrides, and m1 is not overriden by any other method => we keep it
                        elements.add(m1);
                    }
                }
                return elements;
            }


    */

}
