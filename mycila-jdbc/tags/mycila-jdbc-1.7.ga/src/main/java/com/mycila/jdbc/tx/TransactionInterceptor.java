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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TransactionInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = Logger.getLogger(TransactionInterceptor.class.getName());

    private TransactionDefinitionBuilder transactionDefinitionBuilder;
    private TransactionManager transactionManager;

    @Inject
    public void setTransactionDefinitionBuilder(TransactionDefinitionBuilder transactionDefinitionBuilder) {
        this.transactionDefinitionBuilder = transactionDefinitionBuilder;
    }

    @Inject
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionDefinition definition = transactionDefinitionBuilder.build(
                invocation.getMethod(),
                invocation.getThis().getClass());
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("TX: " + invocation.getMethod() + " - begin TX");
        Transaction transaction = transactionManager.beginTransaction(definition);
        TransactionHolder.push(transaction);
        Object retVal;
        try {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("TX: " + invocation.getMethod() + " - proceed...");
            retVal = invocation.proceed();
        } catch (Throwable throwable) {
            if (definition.rollbackOn(throwable)) {
                try {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine("TX: " + invocation.getMethod() + " - rollback because of: " + throwable.getMessage());
                    transaction.rollback();
                } catch (TransactionSystemException ex2) {
                    ex2.initApplicationException(throwable);
                    throw ex2;
                }
            } else {
                // We don't roll back on this exception.
                try {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine("TX: " + invocation.getMethod() + " - commit");
                    transaction.commit();
                } catch (TransactionSystemException ex2) {
                    ex2.initApplicationException(throwable);
                    throw ex2;
                }
            }
            throw throwable;
        } finally {
            TransactionHolder.pop();
        }
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("TX: " + invocation.getMethod() + " - commit");
        transaction.commit();
        return retVal;
    }
}
