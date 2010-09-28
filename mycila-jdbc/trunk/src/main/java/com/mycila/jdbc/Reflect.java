package com.mycila.jdbc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class Reflect<T> {
    private final Map<String, Field> fields = new HashMap<String, Field>();

    private Reflect(Class<?> type) {
        while (type != Object.class) {
            for (Field field : type.getDeclaredFields()) {
                String name = field.getName().toUpperCase();
                if (!fields.containsKey(name)) {
                    fields.put(name, field);
                    if (!field.isAccessible())
                        field.setAccessible(true);
                }
            }
            type = type.getSuperclass();
        }
    }

    public static <T> Reflect<T> access(Class<?> type) {
        return new Reflect<T>(type);
    }

    Field findBest(String name) {
        return fields.get(name);
    }
}
