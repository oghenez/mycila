package com.mycila.jdbc.tx.sql;

import com.mycila.jdbc.ConnectionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Singleton
public final class JdbcConnectionFactory implements ConnectionFactory {

    private final DataSource dataSource;

    @Inject
    public JdbcConnectionFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getCurrentConnection() {
        ConnectionHolder holder = ConnectionHolder.find(dataSource);
        if (holder != null && holder.hasConnection())
            return holder.getConnection();
        throw new IllegalStateException("No Connection bound to local thread !");
    }

    @Override
    public Connection getNewConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
