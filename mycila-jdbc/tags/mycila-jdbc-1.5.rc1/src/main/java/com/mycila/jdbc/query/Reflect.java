/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.jdbc.query;

import com.mycila.jdbc.MycilaJdbcException;

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
            throw new MycilaJdbcException("Error getting field " + field + " from type " + target.getClass() + " : " + e.getMessage(), e);
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
            throw new MycilaJdbcException("Error setting field " + field + " in type " + value.getClass() + " : " + e.getMessage(), e);
        }
    }
}
