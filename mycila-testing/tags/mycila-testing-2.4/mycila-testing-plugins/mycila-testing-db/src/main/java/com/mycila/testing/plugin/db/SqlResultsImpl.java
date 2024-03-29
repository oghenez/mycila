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

import static com.mycila.testing.core.api.Ensure.*;
import com.mycila.testing.plugin.db.api.SqlColumn;
import com.mycila.testing.plugin.db.api.SqlColumnHeader;
import com.mycila.testing.plugin.db.api.SqlData;
import com.mycila.testing.plugin.db.api.SqlResults;
import com.mycila.testing.plugin.db.api.SqlRow;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SqlResultsImpl implements SqlResults {

    private final int columnCount;
    private final List<SqlColumnHeader> sqlColumnHeaders;   // made unmodifiable - getter associated

    private final List<SqlColumn> sqlColumns;               // made unmodifiable - getter associated
    private final Map<String, SqlColumn> sqlColumnsByName;  // modifiable - no getter associated

    private final List<SqlRow> sqlRows;                     // made unmodifiable - getter associated
    private final int rowCount;

    private final List<SqlData[]> sqlDatas;                 // modifiable - no getter associated

    private String headerStr;                               // faster toString()
    private String separator;                               // faster toString()
    private String table;                                   // faster toString()

    private SqlResultsImpl(ResultSet resultSet) {
        try {
            final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // fetch column headers
            this.columnCount = resultSetMetaData.getColumnCount();
            final List<SqlColumnHeader> columnHeaders = new ArrayList<SqlColumnHeader>(columnCount);
            for (int i = 0; i < columnCount; i++) {
                columnHeaders.add(new SqlColumnHeaderImpl(resultSetMetaData, i));
            }
            this.sqlColumnHeaders = Collections.unmodifiableList(columnHeaders);

            // create columns
            this.sqlColumnsByName = new HashMap<String, SqlColumn>(columnCount);
            final List<SqlColumn> columns = new ArrayList<SqlColumn>(columnCount);
            for (int i = 0; i < columnCount; i++) {
                SqlColumn col = new SqlColumnImpl(this, columnHeaders.get(i));
                columns.add(col);
                sqlColumnsByName.put(columnHeaders.get(i).name(), col);
            }
            this.sqlColumns = Collections.unmodifiableList(columns);

            // parse rows
            final int fetch = resultSet.getFetchSize();
            final List<SqlRow> rows = new ArrayList<SqlRow>(fetch);
            this.sqlDatas = new ArrayList<SqlData[]>(fetch);
            int rowIndex = 0;
            for (; resultSet.next(); rowIndex++) {
                final SqlRow sqlRow = new SqlRowImpl(this, rowIndex);
                final SqlData[] row = new SqlData[columnCount];
                rows.add(sqlRow);
                sqlDatas.add(row);
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    row[columnIndex] = new SqlDataImpl(this, sqlRow, columns.get(columnIndex), resultSet);
                }
            }
            this.rowCount = rowIndex;
            this.sqlRows = Collections.unmodifiableList(rows);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public SqlData data(int row, int column) {
        return sqlDatas.get(row)[column];
    }

    public int columnCount() {
        return columnCount;
    }

    public SqlColumn column(int index) {
        return sqlColumns.get(index);
    }

    public SqlColumn column(String name) {
        notNull("Column name", name);
        SqlColumn column = sqlColumnsByName.get(name.toUpperCase());
        if (column == null) {
            throw new IllegalArgumentException("Inexisting column name: " + name);
        }
        return column;
    }

    public List<SqlColumn> columns() {
        return sqlColumns;
    }

    public SqlColumnHeader columnHeader(int index) {
        return sqlColumnHeaders.get(index);
    }

    public List<SqlColumnHeader> columnHeaders() {
        return sqlColumnHeaders;
    }

    public int rowCount() {
        return rowCount;
    }

    public SqlRow row(int index) {
        return sqlRows.get(index);
    }

    public List<SqlRow> rows() {
        return sqlRows;
    }

    static SqlResults cache(ResultSet resultSet) {
        return new SqlResultsImpl(resultSet);
    }

    @Override
    public String toString() {
        if (table == null) {
            StringBuilder sb = addSeparator(addHeader(addSeparator(new StringBuilder())));
            if (rowCount > 0) {
                for (SqlRow sqlRow : sqlRows) {
                    addEntry(sb.append("|"), sqlRow.index() + 1, 5).append("|");
                    for (SqlData data : sqlRow.columns()) {
                        addEntry(sb, data, data.header().displaySize()).append("|");
                    }
                    sb.append("\n");
                }
                addSeparator(sb);
            }
            table = sb.toString();
        }
        return table;
    }

    StringBuilder addHeader(StringBuilder builder) {
        if (headerStr == null) {
            StringBuilder headerBuilder = new StringBuilder("| #     |");
            for (SqlColumnHeader header : columnHeaders()) {
                addEntry(headerBuilder, header.name(), header.displaySize()).append("|");
            }
            headerStr = headerBuilder.append("\n").toString();
        }
        return builder.append(headerStr);
    }

    StringBuilder addSeparator(StringBuilder builder) {
        if (separator == null) {
            StringBuilder sep = new StringBuilder("|-------|");
            for (SqlColumnHeader header : columnHeaders()) {
                char[] c = new char[header.displaySize() + 2];
                Arrays.fill(c, '-');
                sep.append(c).append("|");
            }
            separator = sep.append("\n").toString();
        }
        return builder.append(separator);
    }

    StringBuilder addEntry(StringBuilder str, Object o, int size) {
        String s = String.valueOf(o.toString());
        if (s.length() > size) {
            s = s.substring(0, size - 3) + "...";
        }
        str.append(" ").append(s);
        if (s.length() < size) {
            char[] c = new char[size - s.length()];
            Arrays.fill(c, ' ');
            str.append(c);
        }
        return str.append(" ");
    }

}
