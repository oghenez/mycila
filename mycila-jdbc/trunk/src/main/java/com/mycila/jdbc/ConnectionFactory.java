package com.mycila.jdbc;

import java.sql.Connection;

public interface ConnectionFactory {
    Connection getCurrentConnection();

    Connection getNewConnection();
}
