/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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
package com.mycila.testing.plugin.db;

import com.mycila.testing.plugin.db.api.SqlColumnHeader;
import com.mycila.testing.plugin.db.api.SqlType;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SqlColumnHeaderImpl implements SqlColumnHeader {

    private final int index;
    private final String name;
    private final SqlType sqlType;
    private final String typeName;
    private final int displaySize;

    SqlColumnHeaderImpl(ResultSetMetaData resultSetMetaData, int index) {
        try {
            final int col = index + 1;
            this.index = index;
            this.name = JdbcUtils.lookupColumnName(resultSetMetaData, col).toUpperCase();
            this.sqlType = SqlType.fromSqlType(resultSetMetaData.getColumnType(col));
            this.typeName = resultSetMetaData.getColumnTypeName(col);
            int size = resultSetMetaData.getColumnDisplaySize(col);
            if (size == Integer.MAX_VALUE) {
                size = 20;
            } else if (size < 4) {
                size = 4;
            }
            if (name.length() > size) {
                size = name.length();
            }
            this.displaySize = size;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public int index() {
        return index;
    }

    public String name() {
        return name;
    }

    public int displaySize() {
        return displaySize;
    }

    public SqlType sqlType() {
        return sqlType;
    }

    public String typeName() {
        return typeName;
    }

    public Class<?> type() {
        return sqlType.javaType();
    }

    @Override
    public String toString() {
        return name;
    }
}
