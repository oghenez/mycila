package com.mycila.jdbc.tx;

public interface Transacted {
    boolean isRollbackOnly();

    void setRollbackOnly();

    /**
     * Create a new savepoint. You can roll back to a specific savepoint
     * via <code>rollbackToSavepoint</code>, and explicitly release a
     * savepoint that you don't need anymore via <code>releaseSavepoint</code>.
     * <p>Note that most transaction managers will automatically release
     * savepoints at transaction completion.
     *
     * @return a savepoint object, to be passed into rollbackToSavepoint
     *         or releaseSavepoint
     * @throws TransactionException if the savepoint could not be created,
     *                              for example because the transaction is not in an appropriate state
     * @see java.sql.Connection#setSavepoint
     */
    Object createSavepoint() throws TransactionException;

    /**
     * Roll back to the given savepoint. The savepoint will be
     * automatically released afterwards.
     *
     * @param savepoint the savepoint to roll back to
     * @throws TransactionException if the rollback failed
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    void rollbackToSavepoint(Object savepoint) throws TransactionException;

    /**
     * Explicitly release the given savepoint.
     * <p>Note that most transaction managers will automatically release
     * savepoints at transaction completion.
     * <p>Implementations should fail as silently as possible if
     * proper resource cleanup will still happen at transaction completion.
     *
     * @param savepoint the savepoint to release
     * @throws TransactionException if the release failed
     * @see java.sql.Connection#releaseSavepoint
     */
    void releaseSavepoint(Object savepoint) throws TransactionException;

}
