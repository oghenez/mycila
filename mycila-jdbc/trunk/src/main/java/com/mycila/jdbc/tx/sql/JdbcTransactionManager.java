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

@Singleton
public final class JdbcTransactionManager extends AbstractTransactionManager<TransactedObject> {

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
        return transactedResource.hasConnectionHolder() && transactedResource.getConnectionHolder().isTransactionActive();
    }

    @Override
    protected void doBegin(TransactedObject transactedObject, TransactionDefinition definition) throws TransactionException {
        Connection con = null;
        try {
            if (!transactedObject.hasConnectionHolder())
                transactedObject.newConnectionHolder(new ConnectionHolder(dataSource.getConnection()));

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
        transactedResource.removeConnectionHolder();
        return ConnectionHolder.unbind(dataSource);
    }

    @Override
    protected void doResume(TransactedObject transactedResource, SuspendableResource suspended) throws TransactionException {
        ConnectionHolder.bind(dataSource, (ConnectionHolder) suspended);
    }

    @Override
    protected void doCommit(TransactedObject transactedResource) throws TransactionException {
        try {
            transactedResource.getConnectionHolder().getConnection().commit();
        } catch (SQLException ex) {
            throw new TransactionSystemException("Could not commit JDBC transaction", ex);
        }
    }

    @Override
    protected void doRollback(TransactedObject transactedResource) throws TransactionException {
        try {
            transactedResource.getConnectionHolder().getConnection().rollback();
        }
        catch (SQLException ex) {
            throw new TransactionSystemException("Could not commit JDBC transaction", ex);
        }
    }

    @Override
    protected void doCleanup(TransactedObject transactedResource) throws TransactionException {
        if (transactedResource.isNewConnectionHolder())
            ConnectionHolder.unbind(dataSource);
        Connection connection = transactedResource.getConnectionHolder().getConnection();
        try {
            if (transactedResource.mustRestoreAutoCommit())
                connection.setAutoCommit(true);
            DataSourceUtils.resetConnectionAfterTransaction(connection, transactedResource.getPreviousIsolationLevel());
        }
        catch (Throwable ignored) {
        }
        if (transactedResource.isNewConnectionHolder())
            DataSourceUtils.releaseConnection(connection, this.dataSource);
        transactedResource.getConnectionHolder().clear();
    }

}
