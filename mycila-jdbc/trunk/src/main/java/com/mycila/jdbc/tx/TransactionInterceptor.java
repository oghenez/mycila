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

public final class TransactionInterceptor implements MethodInterceptor {

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
        Transaction transaction = transactionManager.beginTransaction(definition);
        TransactionHolder.push(transaction);
        Object retVal;
        try {
            retVal = invocation.proceed();
        } catch (Throwable throwable) {
            if (definition.rollbackOn(throwable)) {
                try {
                    transaction.rollback();
                }
                catch (TransactionSystemException ex2) {
                    ex2.initApplicationException(throwable);
                    throw ex2;
                }
            } else {
                // We don't roll back on this exception.
                // Will still roll back if TransactionStatus.isRollbackOnly() is true.
                try {
                    transaction.commit();
                }
                catch (TransactionSystemException ex2) {
                    ex2.initApplicationException(throwable);
                    throw ex2;
                }
            }
            throw throwable;
        } finally {
            TransactionHolder.pop();
        }
        transaction.commit();
        return retVal;
    }
}
