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

import com.mycila.jdbc.tx.AbstractTransactionManager;
import com.mycila.jdbc.tx.CannotCreateTransactionException;
import com.mycila.jdbc.tx.SuspendableResource;
import com.mycila.jdbc.tx.TransactionDefinition;
import com.mycila.jdbc.tx.TransactionException;
import com.mycila.jdbc.tx.TransactionSystemException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public final class JdbcTransactionManager extends AbstractTransactionManager<TransactedObject> {

    private static final Logger LOGGER = Logger.getLogger(JdbcTransactionManager.class.getName());

    private final DataSource dataSource;

    @Inject
    public JdbcTransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected TransactedObject doGetCurrent() {
        return new TransactedObject().reusingConnectionHolder(ConnectionHolder.find(dataSource));
    }

    @Override
    protected boolean hasActiveTransaction(TransactedObject transactedResource) {
        boolean b = transactedResource.hasConnectionHolder() && transactedResource.getConnectionHolder().isTransactionActive();
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("hasActiveTransaction: " + b);
        return b;
    }

    @Override
    protected void doBegin(TransactedObject transactedObject, TransactionDefinition definition) throws TransactionException {
        Connection con = null;
        try {
            if (!transactedObject.hasConnectionHolder() || !transactedObject.getConnectionHolder().hasConnection()) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("doBegin: Getting new JDBC connection");
                transactedObject.newConnectionHolder(new ConnectionHolder(dataSource.getConnection()));
            } else {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("doBegin: Reusing previous JDBC connection");
            }

            con = transactedObject.getConnectionHolder().getConnection();

            Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
            transactedObject.setPreviousIsolationLevel(previousIsolationLevel);

            // Switch to manual commit if necessary. This is very expensive in some JDBC drivers,
            // so we don't want to do it unnecessarily (for example if we've explicitly
            // configured the connection pool to set it already).
            if (con.getAutoCommit()) {
                transactedObject.setMustRestoreAutoCommit(true);
                con.setAutoCommit(false);
            }

            transactedObject.getConnectionHolder().setTransactionActive(true);

            if (transactedObject.isNewConnectionHolder())
                ConnectionHolder.bind(dataSource, transactedObject.getConnectionHolder());

        } catch (Exception e) {
            DataSourceUtils.releaseConnection(con, this.dataSource);
            throw new CannotCreateTransactionException("Could not open JDBC Connection for transaction", e);
        }
    }

    @Override
    protected SuspendableResource doSuspend(TransactedObject transactedResource) throws TransactionException {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("doSuspend");
        transactedResource.removeConnectionHolder();
        return ConnectionHolder.unbind(dataSource);
    }

    @Override
    protected void doResume(TransactedObject transactedResource, SuspendableResource suspended) throws TransactionException {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("doResume");
        ConnectionHolder.bind(dataSource, (ConnectionHolder) suspended);
    }

    @Override
    protected void doCommit(TransactedObject transactedResource) throws TransactionException {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("doCommit");
        try {
            transactedResource.getConnectionHolder().getConnection().commit();
        } catch (SQLException ex) {
            throw new TransactionSystemException("Could not commit JDBC transaction", ex);
        }
    }

    @Override
    protected void doRollback(TransactedObject transactedResource) throws TransactionException {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("doRollback");
        try {
            transactedResource.getConnectionHolder().getConnection().rollback();
        } catch (SQLException ex) {
            throw new TransactionSystemException("Could not commit JDBC transaction", ex);
        }
    }

    @Override
    protected void doCleanup(TransactedObject transactedResource) throws TransactionException {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("doCleanup");
        //TODO: not sure - because prevents the ConnectionHolder from being reused and we are using a UnitOfWork already to control connection closing
        //if (transactedResource.isNewConnectionHolder())
        //    ConnectionHolder.unbind(dataSource);
        Connection connection = transactedResource.getConnectionHolder().getConnection();
        try {
            if (transactedResource.mustRestoreAutoCommit())
                connection.setAutoCommit(true);
            DataSourceUtils.resetConnectionAfterTransaction(connection, transactedResource.getPreviousIsolationLevel());
        } catch (Throwable ignored) {
        }
        if (transactedResource.isNewConnectionHolder() && !transactedResource.getConnectionHolder().isTransactionActive())
            DataSourceUtils.releaseConnection(connection, this.dataSource);
        transactedResource.getConnectionHolder().clear();
    }

}
