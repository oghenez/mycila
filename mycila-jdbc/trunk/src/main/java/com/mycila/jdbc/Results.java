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
package com.mycila.jdbc;

import com.google.common.base.Preconditions;
import net.playtouch.jaxspot.repository.sql.SqlException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Results {

    public final List<Column> columns;
    public final List<Row> rows;

    private Results(ResultSet resultSet, Mapper mapper) {
        try {
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int columnCount = resultSet.getMetaData().getColumnCount();
            final int rowCount = resultSet.getFetchSize(); // guess - the value can be higher
            // headers and columns
            {
                columns = new ArrayList<Column>(columnCount);
                for (int c = 0; c < columnCount; c++)
                    columns.add(new Column(new ColumnHeader(metaData, c), rowCount));
            }
            // parse rows and complete cells
            {
                rows = new ArrayList<Row>();
                for (int r = 0; resultSet.next(); r++) {
                    Row row = new Row(r, columnCount, mapper);
                    rows.add(row);
                    for (int c = 0; c < columnCount; c++) {
                        Column column = columns.get(c);
                        Cell cell = new Cell(row, column, resultSet, mapper);
                        row.cells.add(cell);
                        column.cells.add(cell);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public Column column(int columnIndex) {
        return columns.get(columnIndex);
    }

    public Column column(String name) {
        Preconditions.checkNotNull(name, "Column name missing");
        name = name.toUpperCase();
        for (Column column : columns)
            if (column.header.name.equals(name))
                return column;
        throw new NoSuchElementException("Column named " + name);
    }

    public Row uniqueRow() {
        if (rows.size() != 1)
            throw new NoSuchElementException("No unique row returned: got " + rows.size() + " row(s)");
        return rows.get(0);
    }

    public Row row(int rowIndex) {
        return rows.get(rowIndex);
    }

    public Cell cell(int rowIndex, int columnIndex) {
        return row(rowIndex).cell(columnIndex);
    }

    public <T> T unique(Class<T> type) throws NoSuchElementException {
        if (rows.isEmpty())
            throw new NoSuchElementException(type.getName());
        return rows.get(0).map(type);
    }

    public <T> List<T> list(Class<T> type) {
        List<T> list = new ArrayList<T>(rows.size());
        Reflect reflect = Reflect.access(type);
        for (Row row : rows)
            list.add(row.map(type, reflect));
        return list;
    }

    static Results build(ResultSet resultSet, Mapper mapper) {
        return new Results(resultSet, mapper);
    }

}
