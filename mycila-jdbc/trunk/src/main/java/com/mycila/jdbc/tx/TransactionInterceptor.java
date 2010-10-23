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
