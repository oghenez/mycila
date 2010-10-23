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

package com.mycila.jdbc.tx;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AnnotatedTransactionDefinitionBuilder implements TransactionDefinitionBuilder {

    private final Map<Method, TransactionDefinition> cache = new ConcurrentHashMap<Method, TransactionDefinition>();

    @Override
    public TransactionDefinition build(Method method, Class<?> targetClass) {
        {
            TransactionDefinition definition = cache.get(method);
            if (definition != null)
                return definition;
        }
        if (targetClass.getName().contains("$$"))
            targetClass = targetClass.getSuperclass();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        Transactional annot = method.getAnnotation(Transactional.class);
        if (annot == null)
            annot = targetClass.getAnnotation(Transactional.class);
        if (annot != null) {
            def.isolation = annot.isolation();
            def.propagation = annot.propagation();
            def.readOnly = annot.readOnly();
            def.rollbackOn = annot.rollbackOn();
        }
        cache.put(method, def);
        return def;
    }

}
