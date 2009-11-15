package com.mycila.event;

import com.mycila.event.annotation.Publish;
import com.mycila.event.annotation.Reference;
import com.mycila.event.annotation.Subscribe;
import com.mycila.event.annotation.Veto;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.mycila.event.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotationProcessor {

    private final Dispatcher dispatcher;

    private AnnotationProcessor(Dispatcher dispatcher) {
        notNull(dispatcher, "Dispatcher");
        this.dispatcher = dispatcher;
    }

    public <T> T process(Class<T> c) {
        notNull(c, "Class");//TODO
        // interface => jdk proxy
        // abstract ou concrete => cglib
        return null;
    }

    public <T> void inject(T instance) {
        notNull(instance, "Instance");
        Iterable<Method> methods = ClassUtils.getAllDeclaredMethods(instance.getClass());
        for (Method method : ClassUtils.filterAnnotatedMethods(methods, Subscribe.class)) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            dispatcher.subscribe(Topics.anyOf(subscribe.topics()), subscribe.eventType(), new MethodSubscriber(instance, method));
        }
        for (Method method : ClassUtils.filterAnnotatedMethods(methods, Veto.class)) {
            Veto veto = method.getAnnotation(Veto.class);
            dispatcher.register(Topics.anyOf(veto.topics()), veto.eventType(), new MethodVetoer(instance, method));
        }
        for (Method method : ClassUtils.filterAnnotatedMethods(methods, Publish.class)) {
            //TODO
        }
    }

    public static AnnotationProcessor using(Dispatcher dispatcher) {
        return new AnnotationProcessor(dispatcher);
    }

    private static class ReferencableMethod implements Referencable {
        final Reachability reachability;
        final Object target;
        final FastMethod method;

        ReferencableMethod(Object target, Method method) {
            this.method = ClassUtils.fast(method);
            this.target = target;
            this.reachability = method.isAnnotationPresent(Reference.class) ?
                    method.getAnnotation(Reference.class).value() :
                    Reachability.of(target.getClass());
            notNull(reachability, "Value of @Reference on method " + method);
        }

        @Override
        public final Reachability reachability() {
            return reachability;
        }
    }

    private static final class MethodSubscriber<E> extends ReferencableMethod implements Subscriber<E> {
        MethodSubscriber(Object target, Method method) {
            super(target, method);
            uniqueArg(Event.class, method);
            method.setAccessible(true);
        }

        @Override
        public void onEvent(Event<E> event) throws Exception {
            method.invoke(target, new Object[]{event});
        }
    }

    private static final class MethodVetoer<E> extends ReferencableMethod implements Vetoer<E> {
        MethodVetoer(Object target, Method method) {
            super(target, method);
            uniqueArg(VetoableEvent.class, method);
            method.setAccessible(true);
        }

        @Override
        public void check(VetoableEvent<E> vetoableEvent) {
            try {
                method.invoke(target, new Object[]{vetoableEvent});
            } catch (InvocationTargetException t) {
                Throwable e = t.getTargetException();
                if (e instanceof RuntimeException)
                    throw (RuntimeException) e;
                DispatcherException other = new DispatcherException(e.getMessage(), e);
                other.setStackTrace(e.getStackTrace());
                throw other;
            }
        }
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
