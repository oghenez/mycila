package com.mycila.testing.plugin.db.api;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface SqlColumn {
    int index();

    SqlColumnHeader header();

    int rowCount();

    SqlData row(int index);

    List<SqlData> rows();
}
