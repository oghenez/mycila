package com.mycila.jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class Update extends Request<Update> {

    private PreparedStatement statement;

    private Update(Connection connection, String query, String... columnNames) {
        try {
            statement = connection.prepareStatement(query, columnNames);
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public int execute() throws SqlException {
        try {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        } finally {
            JdbcUtils.closeStatement(statement);
        }
    }

    public Results executeAndReturnKeys() throws SqlException {
        try {
            statement.executeUpdate();
            return Results.build(statement.getGeneratedKeys(), mapper);

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

    public static final class Builder {

        private final String query;
        private final Connection connection;

        Builder(Connection connection, String query) {
            this.query = query;
            this.connection = connection;
        }

        /**
         * Execute the mutation and returns the values of the columns containing
         * auto-generated data
         */
        public Update returning(String... columnNames) {
            return new Update(connection, query, columnNames);
        }

        public Update noreturn() {
            return new Update(connection, query);
        }
    }
}
