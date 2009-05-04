package com.mycila.testing.plugin.db.api;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface SqlRow {
    int index();

    int columnCount();

    SqlData column(int index);

    SqlData column(String name);

    List<SqlData> columns();
}
