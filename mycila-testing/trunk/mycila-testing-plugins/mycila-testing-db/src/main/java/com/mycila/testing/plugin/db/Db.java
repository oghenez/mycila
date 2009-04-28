package com.mycila.testing.plugin.db;

import java.sql.Connection;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Db {

    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<Connection>();

    private Db() {
    }

    public static Connection connection() {
        Connection connection = CONNECTION_THREAD_LOCAL.get();
        if (connection == null) {
            throw new IllegalStateException("No Connection bound to local thread. Use @Connection on test methods requiring the creation of a Connection");
        }
        return connection;
    }

    public static void execute(String sql) {
        Connection c;
        c.`
    }

    public static int update(String sql) {

    }
}
