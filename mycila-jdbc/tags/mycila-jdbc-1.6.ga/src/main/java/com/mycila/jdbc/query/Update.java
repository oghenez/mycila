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

package com.mycila.jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class Update extends Request<Update> {

    PreparedStatement statement;

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
        return Results.build(this, mapper);
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
