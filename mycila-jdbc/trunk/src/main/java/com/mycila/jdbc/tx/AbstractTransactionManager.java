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

public abstract class AbstractTransactionManager<T extends Transacted> implements TransactionManager<T> {

    @Override
    public final Transaction<T> beginTransaction(TransactionDefinition definition) throws TransactionException {
        T transactedResource = doGetCurrent();
        // Existing transaction found -> check propagation behavior to find out how to behave.
        if (hasActiveTransaction(transactedResource))
            return handleExistingTransaction(definition, transactedResource);
        // No existing transaction found -> check propagation behavior to find out how to proceed.
        switch (definition.getPropagation()) {
            case MANDATORY: {
                throw new IllegalTransactionStateException("No existing transaction found for transaction marked with propagation 'mandatory'");
            }
            case REQUIRED:
            case REQUIRES_NEW:
            case NESTED: {
                Transaction<T> transaction = new DefaultTransaction<T>(this, definition, true, null, transactedResource);
                doBegin(transactedResource, definition);
                return transaction;
            }
            case NEVER:
            case NOT_SUPPORTED:
            case SUPPORTS: {
                // Create "empty" transaction
                return new DefaultTransaction<T>(this, definition, true, null, null);
            }
        }
        throw new AssertionError("case " + definition.getPropagation());
    }

    private Transaction<T> handleExistingTransaction(TransactionDefinition newDefinition, T transactedResource) throws TransactionException {
        switch (newDefinition.getPropagation()) {
            case NEVER: {
                throw new IllegalTransactionStateException("Existing transaction found for transaction marked with propagation 'never'");
            }
            case NOT_SUPPORTED: {
                SuspendableResource suspendableResource = doSuspend(transactedResource);
                return new DefaultTransaction<T>(this, newDefinition, false, suspendableResource, null);
            }
            case REQUIRES_NEW: {
                SuspendableResource suspended = doSuspend(transactedResource);
                Transaction<T> transaction = new DefaultTransaction<T>(this, newDefinition, true, suspended, transactedResource);
                try {
                    doBegin(transactedResource, newDefinition);
                    return transaction;
                } catch (RuntimeException e) {
                    doResume(transactedResource, suspended);
                    throw e;
                } catch (Error e) {
                    doResume(transactedResource, suspended);
                    throw e;
                }
            }
            case NESTED: {
                DefaultTransaction<T> transaction = new DefaultTransaction<T>(this, newDefinition, false, null, transactedResource);
                transaction.createSavepoint();
                return transaction;
            }
            case SUPPORTS:
            case MANDATORY:
            case REQUIRED: {
                TransactionDefinition currentDefinition = TransactionHolder.current().getDefinition();
                if (currentDefinition.isReadOnly() && !newDefinition.isReadOnly())
                    throw new IllegalTransactionStateException("Participating transaction with definition [" + newDefinition + "] is not marked as read-only but existing transaction is");
                if (newDefinition.getIsolationLevel() != Isolation.DEFAULT
                        && currentDefinition.getIsolationLevel() != newDefinition.getIsolationLevel())
                    throw new IllegalTransactionStateException("Participating transaction with definition [" + newDefinition + "] specifies isolation level which is incompatible with existing transaction: " + currentDefinition.getIsolationLevel());
                return new DefaultTransaction<T>(this, newDefinition, false, null, transactedResource);
            }
        }
        throw new AssertionError("case " + newDefinition.getPropagation());
    }

    final void commit(DefaultTransaction<T> tx) throws TransactionException {
        if (tx.isCompleted())
            throw new IllegalStateException("Transaction is not active");
        if (tx.hasTransactedResource() && tx.getTransactedResource().isRollbackOnly()) {
            rollback(tx);
            // Throw UnexpectedRollbackException only at outermost transaction boundary
            // or if explicitly asked to.
            if (tx.isNew())
                throw new UnexpectedRollbackException("Transaction rolled back because it has been marked as rollback-only");
            return;
        }
        try {
            boolean globalRollbackOnly = false;
            if (tx.isNew())
                globalRollbackOnly = tx.getTransactedResource().isRollbackOnly();
            if (tx.hasSavepoint())
                tx.releaseSavepoint();
            else if (tx.isNew())
                doCommit(tx.getTransactedResource());
            // Throw UnexpectedRollbackException if we have a global rollback-only
            // marker but still didn't get a corresponding exception from commit.
            if (globalRollbackOnly)
                throw new UnexpectedRollbackException("Transaction silently rolled back because it has been marked as rollback-only");
        } catch (UnexpectedRollbackException e) {
            throw e;
        } catch (TransactionException ex) {
            throw ex;
        } catch (RuntimeException e) {
            doRollbackOnCommitException(tx);
            throw e;
        } catch (Error e) {
            doRollbackOnCommitException(tx);
            throw e;
        } finally {
            cleanupAfterCompletion(tx);
        }
    }

    final void rollback(DefaultTransaction<T> tx) throws TransactionException {
        if (tx.isCompleted())
            throw new IllegalStateException("Transaction is not active");
        try {
            if (tx.hasSavepoint())
                tx.rollbackToSavepoint();
            else if (tx.isNew())
                doRollback(tx.getTransactedResource());
            else if (tx.hasTransactedResource())
                tx.getTransactedResource().setRollbackOnly();
        } finally {
            cleanupAfterCompletion(tx);
        }
    }

    private void cleanupAfterCompletion(DefaultTransaction<T> tx) {
        tx.completed = true;
        if (tx.isNew())
            doCleanup(tx.getTransactedResource());
        if (tx.hasSuspendedResource())
            doResume(tx.getTransactedResource(), tx.getSuspendedResource());
    }

    private void doRollbackOnCommitException(Transaction<T> tx) {
        if (tx.isNew())
            doRollback(tx.getTransactedResource());
        else if (tx.hasTransactedResource())
            tx.getTransactedResource().setRollbackOnly();
    }

    protected boolean hasActiveTransaction(T transactedResource) {
        return TransactionHolder.hasTransaction()
                && TransactionHolder.current().hasTransactedResource()
                && TransactionHolder.current().getTransactedResource() == transactedResource;
    }

    protected abstract T doGetCurrent();

    protected abstract void doBegin(T transactedResource, TransactionDefinition definition) throws TransactionException;

    protected abstract SuspendableResource doSuspend(T transactedResource) throws TransactionException;

    protected abstract void doResume(T transactedResource, SuspendableResource suspended) throws TransactionException;

    protected abstract void doCommit(T transactedResource) throws TransactionException;

    protected abstract void doRollback(T transactedResource) throws TransactionException;

    protected abstract void doCleanup(T transactedResource) throws TransactionException;

}
