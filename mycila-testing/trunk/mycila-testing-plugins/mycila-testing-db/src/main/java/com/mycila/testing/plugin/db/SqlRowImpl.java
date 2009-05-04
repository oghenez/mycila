package com.mycila.testing.plugin.db;

import com.mycila.testing.plugin.db.api.SqlData;
import com.mycila.testing.plugin.db.api.SqlRow;

import java.util.AbstractList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SqlRowImpl implements SqlRow {

    private final SqlResultsImpl results;
    private final int index;
    private final List<SqlData> columns;
    private String rowStr;

    SqlRowImpl(SqlResultsImpl results, int index) {
        this.results = results;
        this.index = index;
        this.columns = new AbstractList<SqlData>() {
            @Override
            public SqlData get(int index) {
                return column(index);
            }

            @Override
            public int size() {
                return columnCount();
            }
        };
    }

    public int index() {
        return index;
    }

    public int columnCount() {
        return results.columnCount();
    }

    public SqlData column(int index) {
        return results.data(this.index, index);
    }

    public SqlData column(String name) {
        return results.column(name).row(index);
    }

    public List<SqlData> columns() {
        return columns;
    }

    @Override
    public String toString() {
        if (rowStr == null) {
            StringBuilder sb = results.addSeparator(results.addHeader(results.addSeparator(new StringBuilder())));
            results.addEntry(sb.append("|"), index + 1, 5).append("|");
            for (SqlData data : columns) {
                results.addEntry(sb, data, data.header().displaySize()).append("|");
            }
            rowStr = results.addSeparator(sb.append("\n")).toString();
        }
        return rowStr;
    }

}
