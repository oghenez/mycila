package com.mycila.jdbc;

import net.playtouch.jaxspot.repository.sql.SqlException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public final class Query extends Request<Query> {

    private final PreparedStatement statement;

    Query(Connection connection, String query) {
        try {
            statement = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public <T> T unique(Class<T> type) throws NoSuchElementException, SqlException {
        try {
            statement.setFetchSize(1);
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
        return execute().unique(type);
    }

    public <T> List<T> list(Class<T> type) throws SqlException {
        return execute().list(type);
    }

    public Results execute() throws SqlException {
        try {
            return Results.build(statement.executeQuery(), mapper);
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        } finally {
            JdbcUtils.closeStatement(statement);
        }
    }

    @Override
    PreparedStatement getStatement() {
        return statement;
    }
}
