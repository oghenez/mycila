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

import com.mycila.jdbc.tx.IllegalTransactionStateException;
import com.mycila.jdbc.tx.SuspendableResource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

final class ConnectionHolder implements SuspendableResource {

    private static final String SAVEPOINT_NAME_PREFIX = "SAVEPOINT_";

    private static final ThreadLocal<Map<DataSource, ConnectionHolder>> conHolder = new ThreadLocal<Map<DataSource, ConnectionHolder>>() {
        @Override
        protected Map<DataSource, ConnectionHolder> initialValue() {
            return new IdentityHashMap<DataSource, ConnectionHolder>();
        }
    };

    private Connection connection;
    private long referenceCount = 0;
    private boolean rollbackOnly;
    private long savepointCounter;
    private boolean transactionActive;

    ConnectionHolder(Connection connection) {
        this.connection = connection;
    }

    Connection getConnection() {
        if (connection == null)
            throw new IllegalTransactionStateException("Missing Connection in ConnectionHolder");
        return connection;
    }

    boolean hasConnection() {
        return connection != null;
    }

    void released() {
        if (referenceCount > 0)
            referenceCount--;
        if (!isUsed()) {
            connection = null;
        }
    }

    void used() {
        referenceCount++;
    }

    boolean isUsed() {
        return referenceCount > 0;
    }

    static Collection<ConnectionHolder> listAll() {
        return conHolder.get().values();
    }

    static ConnectionHolder find(DataSource dataSource) {
        return conHolder.get().get(dataSource);
    }

    static void bind(DataSource ds, ConnectionHolder con) {
        conHolder.get().put(ds, con);
    }

    static ConnectionHolder unbind(DataSource ds) {
        return conHolder.get().remove(ds);
    }

    Savepoint createSavepoint() throws SQLException {
        this.savepointCounter++;
        return getConnection().setSavepoint(SAVEPOINT_NAME_PREFIX + this.savepointCounter);
    }

    void clear() {
        this.rollbackOnly = false;
        this.savepointCounter = 0;
        this.transactionActive = false;
    }

    boolean isRollbackOnly() {
        return rollbackOnly;
    }

    void setRollbackOnly() {
        this.rollbackOnly = true;
    }

    boolean isTransactionActive() {
        return transactionActive;
    }

    void setTransactionActive(boolean b) {
        this.transactionActive = b;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
