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

import com.mycila.log.Logger;
import com.mycila.log.Loggers;
import static com.mycila.testing.core.api.Ensure.*;
import com.mycila.testing.ea.ExtendedAssert;
import com.mycila.testing.plugin.db.api.Batch;
import com.mycila.testing.plugin.db.api.Db;

import javax.sql.DataSource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DbImpl implements Db {

    private static final Logger LOGGER = Loggers.get(DbImpl.class);

    private final DataSource dataSource;
    private Connection connection;

    private DbImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Db runScript(String scriptPathInClasspath) {
        notNull("SQL Script path in classpath", scriptPathInClasspath);
        URL res = Thread.currentThread().getContextClassLoader().getResource(scriptPathInClasspath.startsWith("/") ? scriptPathInClasspath.substring(1) : scriptPathInClasspath);
        if (res == null) {
            throw new IllegalArgumentException("Script not found in current context classloader: " + scriptPathInClasspath);
        }
        runScript(res);
        return this;
    }

    public Db runScript(File script) {
        notNull("SQL Script file", script);
        try {
            runScript(script.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return this;
    }

    public Db runScript(URL script) {
        notNull("SQL Script URL", script);
        Statement statement = null;
        try {
            statement = getConnection().createStatement();
            statement.execute(ExtendedAssert.asString(script));
            getConnection().commit();
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException ignored) {
            }
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            JdbcUtils.closeStatement(statement);
        }
        return this;
    }

    public com.mycila.testing.plugin.db.api.Statement prepare(final String query) {
        notNull("SQL query", query);
        return new StatementImpl(this, query);
    }

    public Batch newBatch() {
        return new BatchImpl(this);
    }

    public Db close() {
        if (connection != null) {
            LOGGER.debug("Closing DB connection...");
            JdbcUtils.closeConnection(connection);
            connection = null;
        }
        return this;
    }

    public Connection getConnection() {
        if (connection == null) {
            LOGGER.debug("Opening DB connection...");
            try {
                connection = dataSource.getConnection();
                if (connection.getAutoCommit()) {
                    connection.setAutoCommit(false);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return connection;
    }

    static Db using(DataSource dataSource) {
        return new DbImpl(dataSource);
    }

}
