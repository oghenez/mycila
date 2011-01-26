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

final class DefaultTransaction<T extends Transacted> implements Transaction<T> {

    final TransactionDefinition definition;
    final boolean isNew;
    final long id = TransactionHolder.size();
    final SuspendableResource suspended;
    final AbstractTransactionManager<T> manager;
    final T transactedResource;

    boolean completed;
    private Object savePoint;

    DefaultTransaction(AbstractTransactionManager<T> manager, TransactionDefinition definition, boolean isNew, SuspendableResource suspended, T transactedResource) {
        this.definition = definition;
        this.isNew = isNew;
        this.suspended = suspended;
        this.manager = manager;
        this.transactedResource = transactedResource;
    }

    @Override
    public SuspendableResource getSuspendedResource() {
        if (suspended == null)
            throw new IllegalTransactionStateException("No Suspended transaction found !");
        return suspended;
    }

    @Override
    public boolean hasSuspendedResource() {
        return suspended != null;
    }

    @Override
    public String toString() {
        return "Transaction #" + id;
    }

    @Override
    public TransactionDefinition getDefinition() {
        return definition;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void commit() throws TransactionException {
        manager.commit(this);
    }

    @Override
    public void rollback() throws TransactionException {
        manager.rollback(this);
    }

    @Override
    public boolean hasTransactedResource() {
        return transactedResource != null;
    }

    @Override
    public T getTransactedResource() {
        if (transactedResource == null)
            throw new IllegalTransactionStateException("No Transacted resource found !");
        return transactedResource;
    }

    void createSavepoint() {
        savePoint = getTransactedResource().createSavepoint();
    }

    boolean hasSavepoint() {
        return savePoint != null;
    }

    void rollbackToSavepoint() {
        if (!hasSavepoint())
            throw new TransactionUsageException("No savepoint associated with current transaction");
        getTransactedResource().rollbackToSavepoint(savePoint);
        savePoint = null;
    }

    void releaseSavepoint() {
        if (!hasSavepoint())
            throw new TransactionUsageException("No savepoint associated with current transaction");
        getTransactedResource().releaseSavepoint(savePoint);
        savePoint = null;
    }

}
