package com.mycila.jdbc.query;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Reflect<T> {

    private static final WeakCache<Class<?>, Map<String, Field>> FIELD_CACHE = new WeakCache<Class<?>, Map<String, Field>>(new WeakCache.Provider<Class<?>, Map<String, Field>>() {
        @Override
        public Map<String, Field> get(Class<?> type) {
            if (type == null)
                return Collections.emptyMap();
            if (type == Object.class)
                return Collections.emptyMap();
            Map<String, Field> fields = new HashMap<String, Field>();
            while (type != Object.class) {
                for (Field field : type.getDeclaredFields()) {
                    String name = field.getName().toUpperCase();
                    if (!fields.containsKey(name))
                        fields.put(name, field);
                }
                type = type.getSuperclass();
            }
            return fields;
        }
    });

    private final Map<String, Field> fields;

    private Reflect(Class<T> type) {
        this.fields = FIELD_CACHE.get(type);
    }

    public static <T> Reflect<T> access(Class<T> type) {
        return new Reflect<T>(type);
    }

    public Field findBest(String name) {
        if (name == null)
            throw new IllegalArgumentException("Field name missing");
        return fields.get(name.toUpperCase());
    }

    public <R> R getBest(String name, T target) {
        Field field = findBest(name);
        if (name == null)
            throw new IllegalArgumentException("Field " + name + " not found");
        if (!field.isAccessible())
            field.setAccessible(true);
        try {
            return (R) field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void setBest(String name, T target, Object value) {
        Field field = findBest(name);
        if (name == null)
            throw new IllegalArgumentException("Field " + name + " not found");
        if (!field.isAccessible())
            field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
