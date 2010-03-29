package com.mycila.jmx.export;

import com.mycila.jmx.JmxException;

public final class OperationNotFoundException extends JmxException {
    public OperationNotFoundException(String message) {
        super(message);
    }
}