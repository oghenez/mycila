package com.mycila.jdbc;

public final class SqlException extends RuntimeException {
    public SqlException(String message) {
        super(message);
    }

    public SqlException(String message, Throwable cause) {
        super(message, cause);
        setStackTrace(cause.getStackTrace());
    }

    public SqlException(Throwable cause) {
        super(cause.getMessage(), cause);
        setStackTrace(cause.getStackTrace());
    }
}
