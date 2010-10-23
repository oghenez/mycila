package com.mycila.jdbc.tx;

public abstract class TransactionException extends RuntimeException {

    /**
     * Constructor for TransactionException.
     *
     * @param msg the detail message
     */
    public TransactionException(String msg) {
        super(msg);
    }

    /**
     * Constructor for TransactionException.
     *
     * @param msg   the detail message
     * @param cause the root cause from the transaction API in use
     */
    public TransactionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Check whether this exception contains an exception of the given type:
     * either it is of the given class itself or it contains a nested cause
     * of the given type.
     *
     * @param exType the exception type to look for
     * @return whether there is a nested exception of the specified type
     */
    public boolean contains(Class exType) {
        if (exType == null) {
            return false;
        }
        if (exType.isInstance(this)) {
            return true;
        }
        Throwable cause = getCause();
        if (cause == this) {
            return false;
        } else {
            while (cause != null) {
                if (exType.isInstance(cause)) {
                    return true;
                }
                if (cause.getCause() == cause) {
                    break;
                }
                cause = cause.getCause();
            }
            return false;
        }
    }

}
