package com.mycila.jdbc.tx;

public interface TransactionManager<T extends Transacted> {

    /**
     * Return a currently active transaction or create a new one, according to
     * the specified propagation behavior.
     * <p>Note that parameters like isolation level or timeout will only be applied
     * to new transactions, and thus be ignored when participating in active ones.
     * <p>Furthermore, not all transaction definition settings will be supported
     * by every transaction manager: A proper transaction manager implementation
     * should throw an exception when unsupported settings are encountered.
     * <p>An exception to the above rule is the read-only flag, which should be
     * ignored if no explicit read-only mode is supported. Essentially, the
     * read-only flag is just a hint for potential optimization.
     *
     * @param definition TransactionDefinition instance (can be <code>null</code> for defaults),
     *                   describing propagation behavior, isolation level, timeout etc.
     * @return transaction status object representing the new or current transaction
     */
    Transaction<T> beginTransaction(TransactionDefinition definition) throws TransactionException;

}
