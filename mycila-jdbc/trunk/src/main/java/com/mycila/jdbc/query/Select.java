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
import java.util.List;
import java.util.NoSuchElementException;

public final class Select extends Request<Select> {

    final PreparedStatement statement;

    Select(Connection connection, String query) {
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
        return Results.build(this, mapper);
    }

    @Override
    PreparedStatement getStatement() {
        return statement;
    }

}
