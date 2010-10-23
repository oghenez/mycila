package com.mycila.jdbc.tx;

import java.lang.reflect.Method;

/**
 * Interface used by TransactionInterceptor. Implementations know
 * how to source transaction attributes, whether from configuration,
 * metadata attributes at source level, or anywhere else.
 */
public interface TransactionDefinitionBuilder {

    /**
     * Return the transaction attribute for this method.
     * Return null if the method is non-transactional.
     *
     * @param method      method
     * @param targetClass target class. May be <code>null</code>, in which
     *                    case the declaring class of the method must be used.
     * @return TransactionAttribute the matching transaction attribute,
     *         or <code>null</code> if none found
     */
    TransactionDefinition build(Method method, Class<?> targetClass);

}
