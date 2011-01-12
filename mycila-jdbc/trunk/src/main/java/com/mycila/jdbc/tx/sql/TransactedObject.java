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

package com.mycila.jdbc.tx.sql;

import com.mycila.jdbc.tx.CannotCreateTransactionException;
import com.mycila.jdbc.tx.IllegalTransactionStateException;
import com.mycila.jdbc.tx.Transacted;
import com.mycila.jdbc.tx.TransactionException;
import com.mycila.jdbc.tx.TransactionSystemException;

import java.sql.Savepoint;

final class TransactedObject implements Transacted {

    private ConnectionHolder connectionHolder;
    private boolean isNewConnectionHolder;
    private Integer previousIsolationLevel;
    private boolean mustRestoreAutoCommit;

    boolean hasConnectionHolder() {
        return connectionHolder != null;
    }

    ConnectionHolder getConnectionHolder() {
        if (connectionHolder == null)
            throw new IllegalTransactionStateException("No ConnectionHolder found in current Transacted object");
        return connectionHolder;
    }

    TransactedObject newConnectionHolder(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
        this.isNewConnectionHolder = true;
        return this;
    }

    TransactedObject reusingConnectionHolder(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
        this.isNewConnectionHolder = false;
        return this;
    }

    TransactedObject removeConnectionHolder() {
        this.connectionHolder = null;
        return this;
    }

    boolean isNewConnectionHolder() {
        return isNewConnectionHolder;
    }

    void setPreviousIsolationLevel(Integer previousIsolationLevel) {
        this.previousIsolationLevel = previousIsolationLevel;
    }

    void setMustRestoreAutoCommit(boolean restoreAutoCommit) {
        this.mustRestoreAutoCommit = restoreAutoCommit;
    }

    boolean mustRestoreAutoCommit() {
        return mustRestoreAutoCommit;
    }

    Integer getPreviousIsolationLevel() {
        return previousIsolationLevel;
    }

    @Override
    public boolean isRollbackOnly() {
        return getConnectionHolder().isRollbackOnly();
    }

    @Override
    public void setRollbackOnly(boolean b) {
        getConnectionHolder().setRollbackOnly(b);
    }

    @Override
    public Object createSavepoint() throws TransactionException {
        try {
            return getConnectionHolder().createSavepoint();
        } catch (Throwable ex) {
            throw new CannotCreateTransactionException("Could not create JDBC savepoint", ex);
        }
    }

    @Override
    public void rollbackToSavepoint(Object savepoint) throws TransactionException {
        try {
            getConnectionHolder().getConnection().rollback((Savepoint) savepoint);
        } catch (Throwable ex) {
            throw new TransactionSystemException("Could not roll back to JDBC savepoint", ex);
        }
    }

    @Override
    public void releaseSavepoint(Object savepoint) throws TransactionException {
        try {
            getConnectionHolder().getConnection().releaseSavepoint((Savepoint) savepoint);
        } catch (Throwable ignored) {
        }
    }
}
