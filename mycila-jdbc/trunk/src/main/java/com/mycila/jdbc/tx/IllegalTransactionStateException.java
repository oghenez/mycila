package com.mycila.jdbc.tx;

public class IllegalTransactionStateException extends TransactionException {

    /**
     * Constructor for IllegalTransactionStateException.
     *
     * @param msg the detail message
     */
    public IllegalTransactionStateException(String msg) {
        super(msg);
    }

    /**
     * Constructor for IllegalTransactionStateException.
     *
     * @param msg   the detail message
     * @param cause the root cause from the transaction API in use
     */
    public IllegalTransactionStateException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
