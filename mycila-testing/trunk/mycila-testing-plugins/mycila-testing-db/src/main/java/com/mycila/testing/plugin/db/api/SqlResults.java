package com.mycila.testing.plugin.db.api;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface SqlResults {
    int rowCount();

    SqlRow row(int index);

    List<SqlRow> rows();

    int columnCount();

    SqlColumn column(int index);

    SqlColumn column(String name);

    List<SqlColumn> columns();

    SqlColumnHeader columnHeader(int index);

    List<SqlColumnHeader> columnHeaders();

    SqlData data(int row, int column);
}
