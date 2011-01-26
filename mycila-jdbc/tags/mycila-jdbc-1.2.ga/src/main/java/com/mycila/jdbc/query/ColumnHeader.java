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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public final class ColumnHeader {

    public final String name;
    public final String typeName;
    public final int index;
    public final SqlType sqlType;

    ColumnHeader(ResultSetMetaData metadata, int index) {
        this.index = index;
        try {
            index++;
            this.name = JdbcUtils.lookupColumnName(metadata, index).toUpperCase();
            this.sqlType = SqlType.fromSqlType(metadata.getColumnType(index));
            this.typeName = metadata.getColumnTypeName(index);
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }

    }

    public Class<?> type() {
        return sqlType.javaType();
    }

    @Override
    public String toString() {
        return name;
    }
}
