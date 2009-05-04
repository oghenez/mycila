package com.mycila.testing.plugin.db;

import static com.mycila.testing.core.api.Ensure.*;
import com.mycila.testing.ea.ExtendedAssert;
import com.mycila.testing.plugin.db.api.Batch;
import com.mycila.testing.plugin.db.api.Db;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class BatchImpl implements Batch {

    private final Statement statement;
    private final Db db;

    BatchImpl(Db db) {
        this.db = db;
        try {
            statement = db.getConnection().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Batch add(String sqlLines) {
        notNull("SQL commands", sqlLines);
        try {
            statement.addBatch(sqlLines);
        } catch (SQLException e) {
            JdbcUtils.closeStatement(statement);
            throw new RuntimeException(e.getMessage(), e);
        }
        return this;
    }

    public Batch addScript(String scriptPathInClasspath) {
        notNull("SQL Script path in classpath", scriptPathInClasspath);
        URL res = Thread.currentThread().getContextClassLoader().getResource(scriptPathInClasspath.startsWith("/") ? scriptPathInClasspath.substring(1) : scriptPathInClasspath);
        if (res == null) {
            throw new IllegalArgumentException("Script not found in current context classloader: " + scriptPathInClasspath);
        }
        addScript(res);
        return this;
    }

    public Batch addScript(File script) {
        notNull("SQL Script file", script);
        try {
            addScript(script.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return this;
    }

    public Batch addScript(URL script) {
        notNull("SQL Script URL", script);
        add(ExtendedAssert.asString(script));
        return this;
    }

    public Db commit() {
        try {
            statement.executeBatch();
            db.getConnection().commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            JdbcUtils.closeStatement(statement);
        }
        return db;
    }

    public Db rollback() {
        try {
            db.getConnection().rollback();
        } catch (SQLException ignored) {
        } finally {
            JdbcUtils.closeStatement(statement);
        }
        return db;
    }
}
