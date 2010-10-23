package com.mycila.jdbc.tx;

/**
 * Representation of the status of a transaction.
 * <p/>
 * <p>Transactional code can use this to retrieve status information,
 * and to programmatically request a rollback (instead of throwing
 * an exception that causes an implicit rollback).
 * <p/>
 * <p>Derives from the SavepointManager interface to provide access
 * to savepoint management facilities. Note that savepoint management
 * is only available if supported by the underlying transaction manager.
 */
public interface Transaction<T extends Transacted> {

    TransactionDefinition getDefinition();

    boolean hasTransactedResource();

    T getTransactedResource();

    boolean hasSuspendedResource();

    SuspendableResource getSuspendedResource();

    /**
     * Return whether the present transaction is new (else participating
     * in an existing transaction, or potentially not running in an
     * actual transaction in the first place).
     */
    boolean isNew();

    boolean isCompleted();

    void commit() throws TransactionException;

    void rollback() throws TransactionException;

}
