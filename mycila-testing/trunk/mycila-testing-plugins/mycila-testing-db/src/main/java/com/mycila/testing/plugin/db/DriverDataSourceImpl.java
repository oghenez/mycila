package com.mycila.testing.plugin.db;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DriverDataSourceImpl implements DataSource {

    private final String url;
    private String username;
    private String password;
    private Properties connectionProperties = new Properties();

    private DriverDataSourceImpl(String url) {
        this.url = url;
    }

    public DriverDataSourceImpl withUsername(String username) {
        this.username = username;
        return this;
    }

    public DriverDataSourceImpl withPassword(String password) {
        this.password = password;
        return this;
    }

    public DriverDataSourceImpl withProperty(String property, Object value) {
        connectionProperties.put(property, value);
        return this;
    }

    public Connection getConnection() throws SQLException {
        return getConnectionFromDriverManager(username, password);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return getConnectionFromDriverManager(username, password);
    }

    private Connection getConnectionFromDriverManager(String username, String password) throws SQLException {
        if (username != null) {
            connectionProperties.setProperty("user", username);
        }
        if (password != null) {
            connectionProperties.setProperty("password", password);
        }
        return DriverManager.getConnection(url, connectionProperties);
    }

    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException("getLogWriter");
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException("setLogWriter");
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException("setLoginTimeout");
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    static DriverDataSourceImpl from(String url) {
        return new DriverDataSourceImpl(url);
    }

    static DriverDataSourceImpl from(InjectDataSource driverDataSource) {
        try {
            Class.forName(driverDataSource.driver().getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        DriverDataSourceImpl dataSource = new DriverDataSourceImpl(driverDataSource.url())
                .withUsername(driverDataSource.username())
                .withPassword(driverDataSource.password());
        for (Property property : driverDataSource.properties()) {
            dataSource.withProperty(property.name(), property.value());
        }
        return dataSource;
    }
}
