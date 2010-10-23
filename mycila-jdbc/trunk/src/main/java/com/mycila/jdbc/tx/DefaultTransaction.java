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
