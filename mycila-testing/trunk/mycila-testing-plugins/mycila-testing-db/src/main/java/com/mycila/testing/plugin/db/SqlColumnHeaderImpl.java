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
