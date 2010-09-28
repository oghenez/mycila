package com.mycila.jdbc;

import java.sql.Connection;

public final class Sql {

    private final Connection connection;

    public Sql(Connection connection) {
        this.connection = connection;
    }

    public Query query(String query) {
        return new Query(connection, query);
    }

    public Insert insert(String table) {
        return new Insert(connection, table);
    }

    public Update.Builder update(String query) {
        return new Update.Builder(connection, query);
    }

}
