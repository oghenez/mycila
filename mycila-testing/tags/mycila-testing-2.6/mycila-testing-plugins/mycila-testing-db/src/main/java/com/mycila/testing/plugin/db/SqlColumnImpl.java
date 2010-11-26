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

import com.mycila.testing.plugin.db.api.SqlColumn;
import com.mycila.testing.plugin.db.api.SqlColumnHeader;
import com.mycila.testing.plugin.db.api.SqlData;
import com.mycila.testing.plugin.db.api.SqlResults;

import java.util.AbstractList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SqlColumnImpl implements SqlColumn {

    private final SqlResults results;
    private final SqlColumnHeader columnHeader;
    private final List<SqlData> rows;

    SqlColumnImpl(SqlResults results, SqlColumnHeader columnHeader) {
        this.results = results;
        this.columnHeader = columnHeader;
        this.rows = new AbstractList<SqlData>() {
            @Override
            public SqlData get(int index) {
                return row(index);
            }

            @Override
            public int size() {
                return rowCount();
            }
        };
    }

    public int index() {
        return columnHeader.index();
    }

    public SqlColumnHeader header() {
        return columnHeader;
    }

    public int rowCount() {
        return results.rowCount();
    }

    public SqlData row(int index) {
        return results.data(index, columnHeader.index());
    }

    public List<SqlData> rows() {
        return rows;
    }

    @Override
    public String toString() {
        return columnHeader.name() + " : " + rows;
    }
}
