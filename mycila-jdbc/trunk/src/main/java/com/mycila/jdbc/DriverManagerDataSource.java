package com.mycila.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DriverManagerDataSource implements DataSource {

    private String url;
    private String username;
    private String password;
    private Properties connectionProperties = new Properties();

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = new Properties(connectionProperties);
    }

    public void setDriverClassName(String clazz) {
        try {
            Class.forName(clazz);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load JDBC driver class [" + clazz + "]", e);
        }
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

    @SuppressWarnings({"unchecked"})
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (!DataSource.class.equals(iface)) {
            throw new SQLException("DataSource of type [" + getClass().getName() + "] can only be unwrapped as [javax.sql.DataSource], not as [" + iface.getName());
        }
        return (T) this;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return DataSource.class.equals(iface);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public static DriverManagerDataSourceBuilder use(Class<? extends Driver> driver) {
        return new DriverManagerDataSourceBuilder();
    }

    public static class DriverManagerDataSourceBuilder {
        private final DriverManagerDataSource ds = new DriverManagerDataSource();

        public DriverManagerDataSourceBuilder withUrl(String url) {
            ds.setUrl(url);
            return this;
        }

        public DriverManagerDataSourceBuilder withUsername(String username) {
            ds.setUsername(username);
            return this;
        }

        public DriverManagerDataSourceBuilder withPassword(String password) {
            ds.setPassword(password);
            return this;
        }

        public DriverManagerDataSourceBuilder withProperty(String name, String value) {
            ds.connectionProperties.setProperty(name, value);
            return this;
        }

        public DriverManagerDataSource build() {
            return ds;
        }
    }
}