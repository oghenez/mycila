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
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

final class ConnectionHolder implements SuspendableResource {

    private static final Logger LOGGER = Logger.getLogger(ConnectionHolder.class.getName());
    private static final String SAVEPOINT_NAME_PREFIX = "SAVEPOINT_";

    static final ThreadLocal<Map<DataSource, ConnectionHolder>> conHolder = new ThreadLocal<Map<DataSource, ConnectionHolder>>() {
        @Override
        protected Map<DataSource, ConnectionHolder> initialValue() {
            return new IdentityHashMap<DataSource, ConnectionHolder>();
        }
    };

    private final String thread;
    private Connection connection;
    private long referenceCount = 0;
    private boolean rollbackOnly;
    private long savepointCounter;
    private boolean transactionActive;

    ConnectionHolder(Connection connection) {
        this.connection = connection;
        this.thread = Thread.currentThread().getName();
    }

    @Override
    public String toString() {
        return "ConnectionHolder[connection=" + connection + ",thread=" + thread + ",referenceCount=" + referenceCount + ",transactionActive=" + transactionActive + "]";
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
        return new ArrayList<ConnectionHolder>(conHolder.get().values());
    }

    static ConnectionHolder find(DataSource dataSource) {
        return conHolder.get().get(dataSource);
    }

    static void bind(DataSource ds, ConnectionHolder con) {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Binding ConnectionHolder " + con + " to DataSource " + ds);
        conHolder.get().put(ds, con);
    }

    static ConnectionHolder unbind(DataSource ds) {
        ConnectionHolder holder = conHolder.get().remove(ds);
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Unbinding ConnectionHolder " + holder + " from DataSource " + ds);
        return holder;
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

    void setRollbackOnly(boolean b) {
        this.rollbackOnly = b;
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

    public static void clean() {
        if (!conHolder.get().isEmpty())
            throw new IllegalStateException("Cleaning of ConnectionHolder failed: some connections are still there and have not been cleared: " + conHolder.get());
        conHolder.remove();
    }

    public void close() {
        if (hasConnection()) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("Closing JDBC connection");
            try {
                getConnection().close();
            } catch (SQLException e) {
                LOGGER.log(Level.FINE, "Error closing connection: " + e.getMessage(), e);
            } finally {
                connection = null;
            }
        }
        for (Map.Entry<DataSource, ConnectionHolder> entry : new IdentityHashMap<DataSource, ConnectionHolder>(conHolder.get()).entrySet()) {
            if (entry.getValue() == this) {
                unbind(entry.getKey());
            }
        }
    }
}
