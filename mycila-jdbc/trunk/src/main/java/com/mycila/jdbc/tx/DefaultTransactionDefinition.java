package com.mycila.jdbc.tx;

import java.util.Arrays;

final class DefaultTransactionDefinition implements TransactionDefinition {

    Propagation propagation = Propagation.REQUIRED;
    Isolation isolation = Isolation.DEFAULT;
    boolean readOnly = false;
    Class<?>[] rollbackOn = new Class[]{Throwable.class};

    @Override
    public Propagation getPropagation() {
        return propagation;
    }

    @Override
    public Isolation getIsolationLevel() {
        return isolation;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public boolean rollbackOn(Throwable ex) {
        for (Class<?> c : rollbackOn)
            if (c.isInstance(ex))
                return true;
        return false;
    }

    @Override
    public String toString() {
        return "TransactionDefinition{" +
                "isolation=" + isolation +
                ", propagation=" + propagation +
                ", readOnly=" + readOnly +
                ", rollback=" + (rollbackOn == null ? null : Arrays.asList(rollbackOn)) +
                '}';
    }
}
