package com.mycila.jdbc.tx.sql;

import com.mycila.jdbc.UnitOfWork;

import javax.inject.Singleton;
import java.sql.SQLException;

@Singleton
public final class JdbcUnitOfWork implements UnitOfWork {
    @Override
    public void begin() {
    }

    @Override
    public void end() {
        for (ConnectionHolder holder : ConnectionHolder.listAll())
            if (holder != null && holder.hasConnection())
                try {
                    holder.getConnection().close();
                } catch (SQLException ignored) {
                }
    }
}
