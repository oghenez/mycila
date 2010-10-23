package com.mycila.jdbc.tx.sql;

import com.mycila.jdbc.tx.Isolation;
import com.mycila.jdbc.tx.TransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

final class DataSourceUtils {

    private DataSourceUtils() {
    }

    static Connection getConnection(DataSource dataSource) throws SQLException {
        ConnectionHolder conHolder = ConnectionHolder.find(dataSource);
        if (conHolder == null) {
            ConnectionHolder.bind(dataSource, conHolder = new ConnectionHolder(dataSource.getConnection()));
        }
        if (!conHolder.hasConnection()) {
            //Fetching resumed JDBC Connection from DataSource
            conHolder.setConnection(dataSource.getConnection());
        }
        conHolder.used();
        return conHolder.getConnection();
    }

    static void resetConnectionAfterTransaction(Connection con, Integer previousIsolationLevel) {
        try {
            // Reset transaction isolation to previous value, if changed for the transaction.
            if (previousIsolationLevel != null)
                con.setTransactionIsolation(previousIsolationLevel);
            // Reset read-only flag.
            if (con.isReadOnly())
                con.setReadOnly(false);
        }
        catch (Throwable ignored) {
        }
    }

    static Integer prepareConnectionForTransaction(Connection con, TransactionDefinition definition) throws SQLException {
        // Set read-only flag.
        if (definition.isReadOnly()) {
            try {
                con.setReadOnly(true);
            }
            catch (SQLException ex) {
                Throwable exToCheck = ex;
                while (exToCheck != null) {
                    if (exToCheck.getClass().getSimpleName().contains("Timeout")) {
                        // Assume it's a connection timeout that would otherwise get lost: e.g. from JDBC 4.0
                        throw ex;
                    }
                    exToCheck = exToCheck.getCause();
                }
                // "read-only not supported" SQLException -> ignore, it's just a hint anyway
            }
            catch (RuntimeException ex) {
                Throwable exToCheck = ex;
                while (exToCheck != null) {
                    if (exToCheck.getClass().getSimpleName().contains("Timeout")) {
                        // Assume it's a connection timeout that would otherwise get lost: e.g. from Hibernate
                        throw ex;
                    }
                    exToCheck = exToCheck.getCause();
                }
                // "read-only not supported" UnsupportedOperationException -> ignore, it's just a hint anyway
            }
        }

        // Apply specific isolation level, if any.
        Integer previousIsolationLevel = null;
        if (definition.getIsolationLevel() != Isolation.DEFAULT) {
            int currentIsolation = con.getTransactionIsolation();
            if (currentIsolation != definition.getIsolationLevel().value()) {
                previousIsolationLevel = currentIsolation;
                con.setTransactionIsolation(definition.getIsolationLevel().value());
            }
        }
        return previousIsolationLevel;
    }

    static void releaseConnection(Connection con, DataSource dataSource) {
        try {
            if (con == null) {
                return;
            }
            if (dataSource != null) {
                ConnectionHolder conHolder = ConnectionHolder.find(dataSource);
                if (conHolder != null && conHolder.hasConnection() && con == conHolder.getConnection()) {
                    // It's the transactional Connection: Don't close it.
                    conHolder.released();
                    return;
                }
            }
            con.close();
        }
        catch (Throwable ignored) {
        }
    }

}
