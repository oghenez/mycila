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

package old;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Singleton;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.Scoping;
import com.google.inject.internal.Sets;
import com.google.inject.matcher.Matcher;
import com.google.inject.name.Names;
import org.guiceyfruit.support.CloseErrors;
import org.guiceyfruit.support.CloseFailedException;
import org.guiceyfruit.support.Closer;
import org.guiceyfruit.support.Closers;
import org.guiceyfruit.support.CompositeCloser;
import org.guiceyfruit.support.HasScopeAnnotation;
import org.guiceyfruit.support.internal.CloseErrorsImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class Injector implements com.google.inject.Injector {

    private final com.google.inject.Injector injector;

    private Injector(com.google.inject.Injector injector) {
        this.injector = injector;
    }

    public void fireStarted() {
        for (Binding<?> binding : getAllBindings().values())
            if (OnStartSingleton.class.equals(getScopeAnnotation(binding)))
                binding.getProvider().get();
    }

    // From Guicey Injectors class

    public void close() throws CloseFailedException {
        close(new CloseErrorsImpl(Injector.class));
    }

    public void close(CloseErrors errors) throws CloseFailedException {
        close(Singleton.class, errors);
        close(LazySingleton.class, errors);
        close(OnStartSingleton.class, errors);
    }

    public void close(Class<? extends Annotation> scopeAnnotationToClose) throws CloseFailedException {
        close(scopeAnnotationToClose, new CloseErrorsImpl(Injector.class));
    }

    public void close(Class<? extends Annotation> scopeAnnotationToClose, CloseErrors errors) throws CloseFailedException {
        Set<Closer> closers = getInstancesOf(Closer.class);
        Closer closer = CompositeCloser.newInstance(closers);
        if (closer == null) return;

        Set<Map.Entry<Key<?>, Binding<?>>> entries = getAllBindings().entrySet();
        for (Map.Entry<Key<?>, Binding<?>> entry : entries) {
            Key<?> key = entry.getKey();
            Binding<?> binding = entry.getValue();
            closeBinding(key, binding, scopeAnnotationToClose, closer, errors);
        }

        tryCloseJitBindings(closer, scopeAnnotationToClose, errors);
        errors.throwIfNecessary();
    }

    @SuppressWarnings({"unchecked"})
    private void tryCloseJitBindings(Closer closer, Class<? extends Annotation> scopeAnnotationToClose, CloseErrors errors) {
        Class<? extends com.google.inject.Injector> type = injector.getClass();
        Field field;
        try {
            field = type.getDeclaredField("jitBindings");
            field.setAccessible(true);
            Object bindings = field.get(injector);
            if (bindings != null) {
                if (bindings instanceof Map) {
                    Map<Key<?>, BindingImpl<?>> map = (Map<Key<?>, BindingImpl<?>>) bindings;
                    Set<Map.Entry<Key<?>, BindingImpl<?>>> entries = map.entrySet();
                    for (Map.Entry<Key<?>, BindingImpl<?>> entry : entries) {
                        closeBinding(entry.getKey(), entry.getValue(), scopeAnnotationToClose, closer, errors);
                    }
                }
            }
        }
        catch (NoSuchFieldException e) {
            // ignore - Guice has refactored so we can't access the jit bindings
            // System.out.println("No such field! " + e);
        }
        catch (IllegalAccessException e) {
            // ignore - Guice has refactored so we can't access the jit bindings
            //System.out.println("Failed to access field: " + field + ". Reason: " + e);
        }
    }

    private void closeBinding(Key<?> key, Binding<?> binding, Class<? extends Annotation> scopeAnnotationToClose, Closer closer, CloseErrors errors) {
        Provider<?> provider = binding.getProvider();
        Class<? extends Annotation> scopeAnnotation = getScopeAnnotation(binding);
        if (scopeAnnotation != null && scopeAnnotation.equals(scopeAnnotationToClose)) {
            Object value = provider.get();
            if (value != null) {
                Closers.close(key, value, closer, errors);
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    public <T> Binding<T> getBinding(TypeLiteral<T> requiredType, String name) {
        return (Binding<T>) getAllBindings().get(Key.get(requiredType, Names.named(name)));
    }

    @SuppressWarnings({"unchecked"})
    public <T> Binding<T> getBinding(TypeLiteral<T> requiredType) {
        return (Binding<T>) getAllBindings().get(Key.get(requiredType));
    }

    public Set<Binding<?>> getBindingsAnnotatedwith(Annotation annotation) {
        Set<Binding<?>> answer = Sets.newHashSet();
        Set<Map.Entry<Key<?>, Binding<?>>> entries = getAllBindings().entrySet();
        for (Map.Entry<Key<?>, Binding<?>> entry : entries) {
            if (entry.getKey().getAnnotation() != null
                    && entry.getKey().getAnnotation().equals(annotation))
                answer.add(entry.getValue());
        }
        return answer;
    }

    public Set<Binding<?>> getBindingsAnnotatedwith(Class<? extends Annotation> annotationType) {
        Set<Binding<?>> answer = Sets.newHashSet();
        Set<Map.Entry<Key<?>, Binding<?>>> entries = getAllBindings().entrySet();
        for (Map.Entry<Key<?>, Binding<?>> entry : entries) {
            if (entry.getKey().getAnnotationType() != null
                    && entry.getKey().getAnnotationType().equals(annotationType))
                answer.add(entry.getValue());
        }
        return answer;
    }

    public Set<Binding<?>> getBindingsOf(Class<?> baseClass) {
        Set<Binding<?>> answer = Sets.newHashSet();
        Set<Map.Entry<Key<?>, Binding<?>>> entries = getAllBindings().entrySet();
        for (Map.Entry<Key<?>, Binding<?>> entry : entries) {
            Key<?> key = entry.getKey();
            Class<?> keyType = getKeyType(key);
            if (keyType != null && baseClass.isAssignableFrom(keyType))
                answer.add(entry.getValue());
        }
        return answer;
    }

    public Set<Binding<?>> getBindingsOf(Matcher<Class> matcher) {
        Set<Binding<?>> answer = Sets.newHashSet();
        Set<Map.Entry<Key<?>, Binding<?>>> entries = getAllBindings().entrySet();
        for (Map.Entry<Key<?>, Binding<?>> entry : entries) {
            Key<?> key = entry.getKey();
            Class<?> keyType = getKeyType(key);
            if (keyType != null && matcher.matches(keyType))
                answer.add(entry.getValue());
        }
        return answer;
    }

    public <T> T getInstance(Class<T> type, String name) {
        return injector.getInstance(Key.get(type, Names.named(name)));
    }

    public <T> Set<T> getInstancesOf(Class<T> baseClass) {
        Set<T> answer = Sets.newHashSet();
        Set<Map.Entry<Key<?>, Binding<?>>> entries = getAllBindings().entrySet();
        for (Map.Entry<Key<?>, Binding<?>> entry : entries) {
            Key<?> key = entry.getKey();
            Class<?> keyType = getKeyType(key);
            if (keyType != null && baseClass.isAssignableFrom(keyType)) {
                Binding<?> binding = entry.getValue();
                Object value = binding.getProvider().get();
                if (value != null) {
                    T castValue = baseClass.cast(value);
                    answer.add(castValue);
                }
            }
        }
        return answer;
    }

    @SuppressWarnings({"unchecked"})
    public <T> Set<T> getInstancesOf(Matcher<Class> matcher) {
        Set<T> answer = Sets.newHashSet();
        Set<Map.Entry<Key<?>, Binding<?>>> entries = getAllBindings().entrySet();
        for (Map.Entry<Key<?>, Binding<?>> entry : entries) {
            Key<?> key = entry.getKey();
            Class<?> keyType = getKeyType(key);
            if (keyType != null && matcher.matches(keyType)) {
                Binding<?> binding = entry.getValue();
                Object value = binding.getProvider().get();
                answer.add((T) value);
            }
        }
        return answer;
    }

    public Class<?> getKeyType(Key<?> key) {
        Class<?> keyType = null;
        TypeLiteral<?> typeLiteral = key.getTypeLiteral();
        Type type = typeLiteral.getType();
        if (type instanceof Class)
            keyType = (Class<?>) type;
        return keyType;
    }

    @SuppressWarnings({"unchecked"})
    public <T> Set<Provider<T>> getProvidersOf(Class<T> baseClass) {
        Set<Provider<T>> answer = Sets.newHashSet();
        Set<Map.Entry<Key<?>, Binding<?>>> entries = getAllBindings().entrySet();
        for (Map.Entry<Key<?>, Binding<?>> entry : entries) {
            Key<?> key = entry.getKey();
            Class<?> keyType = getKeyType(key);
            if (keyType != null && baseClass.isAssignableFrom(keyType)) {
                Binding<?> binding = entry.getValue();
                answer.add((Provider<T>) binding.getProvider());
            }
        }
        return answer;
    }

    @SuppressWarnings({"unchecked"})
    public <T> Set<Provider<T>> getProvidersOf(Matcher<Class> matcher) {
        Set<Provider<T>> answer = Sets.newHashSet();
        Set<Map.Entry<Key<?>, Binding<?>>> entries = getAllBindings().entrySet();
        for (Map.Entry<Key<?>, Binding<?>> entry : entries) {
            Key<?> key = entry.getKey();
            Class<?> keyType = getKeyType(key);
            if (keyType != null && matcher.matches(keyType)) {
                Binding<?> binding = entry.getValue();
                answer.add((Provider<T>) binding.getProvider());
            }
        }
        return answer;
    }

    public Class<? extends Annotation> getScopeAnnotation(Binding<?> binding) {
        Class<? extends Annotation> scopeAnnotation = null;
        if (binding instanceof BindingImpl) {
            BindingImpl bindingImpl = (BindingImpl) binding;
            Scoping scoping = bindingImpl.getScoping();
            if (scoping != null) {
                scopeAnnotation = scoping.getScopeAnnotation();
                if (scopeAnnotation == null) {
                    Scope scope = scoping.getScopeInstance();
                    if (scope instanceof HasScopeAnnotation) {
                        HasScopeAnnotation hasScopeAnnotation = (HasScopeAnnotation) scope;
                        scopeAnnotation = hasScopeAnnotation.getScopeAnnotation();
                    }
                    if (scopeAnnotation == null && (scoping == Scoping.EAGER_SINGLETON
                            || scoping == Scoping.SINGLETON_ANNOTATION
                            || scoping == Scoping.SINGLETON_INSTANCE)) {
                        scopeAnnotation = Singleton.class;
                    }
                }
            }
        }
        return scopeAnnotation;
    }

    public boolean hasBinding(Class<?> baseClass) {
        return !getBindingsOf(baseClass).isEmpty();
    }

    public boolean hasBinding(Key<?> key) {
        return getAllBindings().get(key) != null;
    }

    public boolean hasBinding(Matcher<Class> matcher) {
        return !getBindingsOf(matcher).isEmpty();
    }

    public Map<Key<?>, Binding<?>> getAllBindings() {
        Map<Key<?>, Binding<?>> all = new LinkedHashMap<Key<?>, Binding<?>>();
        com.google.inject.Injector i = injector;
        while (i != null) {
            all.putAll(i.getBindings());
            i = i.getParent();
        }
        return all;
    }

    // delegates of guice Injector

    public Injector createChildInjector(Iterable<? extends Module> modules) {
        return wrap(injector.createChildInjector(modules));
    }

    public Injector createChildInjector(Module... modules) {
        return wrap(injector.createChildInjector(modules));
    }

    public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type) {
        return injector.findBindingsByType(type);
    }

    public <T> Binding<T> getBinding(Key<T> key) {
        return injector.getBinding(key);
    }

    public <T> Binding<T> getBinding(Class<T> type) {
        return injector.getBinding(type);
    }

    public Map<Key<?>, Binding<?>> getBindings() {
        return injector.getBindings();
    }

    public <T> T getInstance(Key<T> key) {
        return injector.getInstance(key);
    }

    public <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }

    public <T> MembersInjector<T> getMembersInjector(Class<T> type) {
        return injector.getMembersInjector(type);
    }

    public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
        return injector.getMembersInjector(typeLiteral);
    }

    public Injector getParent() {
        return wrap(injector.getParent());
    }

    public <T> Provider<T> getProvider(Key<T> key) {
        return injector.getProvider(key);
    }

    public <T> Provider<T> getProvider(Class<T> type) {
        return injector.getProvider(type);
    }

    public void injectMembers(Object instance) {
        injector.injectMembers(instance);
    }

    public static Injector create(Stage stage, Module... modules) {
        return wrap(Guice.createInjector(stage, modules));
    }

    public static Injector create(Stage stage, Iterable<Module> modules) {
        return wrap(Guice.createInjector(stage, modules));
    }

    public static Injector wrap(com.google.inject.Injector injector) {
        return new Injector(injector);
    }

}
