package com.mycila.jdbc;

public interface UnitOfWork {
    void begin();

    void end();
}
