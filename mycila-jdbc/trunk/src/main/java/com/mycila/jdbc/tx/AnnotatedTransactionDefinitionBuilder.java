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
