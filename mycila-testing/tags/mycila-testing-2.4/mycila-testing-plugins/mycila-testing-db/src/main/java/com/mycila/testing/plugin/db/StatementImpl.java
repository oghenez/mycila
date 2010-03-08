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

import com.mycila.testing.plugin.db.api.Db;
import com.mycila.testing.plugin.db.api.SqlResults;
import com.mycila.testing.plugin.db.api.SqlType;
import com.mycila.testing.plugin.db.api.Statement;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class StatementImpl implements Statement {

    final DbImpl db;
    final PreparedStatement preparedStatement;

    StatementImpl(DbImpl db, String sql) {
        this.db = db;
        try {
            preparedStatement = db.getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setNull(int parameterIndex) {
        try {
            preparedStatement.setNull(parameterIndex, SqlType.NULL.sqlType());
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setBoolean(int parameterIndex, boolean x) {
        try {
            preparedStatement.setBoolean(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setByte(int parameterIndex, byte x) {
        try {
            preparedStatement.setByte(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setShort(int parameterIndex, short x) {
        try {
            preparedStatement.setShort(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setInt(int parameterIndex, int x) {
        try {
            preparedStatement.setInt(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setLong(int parameterIndex, long x) {
        try {
            preparedStatement.setLong(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setFloat(int parameterIndex, float x) {
        try {
            preparedStatement.setFloat(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setDouble(int parameterIndex, double x) {
        try {
            preparedStatement.setDouble(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setBigDecimal(int parameterIndex, BigDecimal x) {
        try {
            preparedStatement.setBigDecimal(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setString(int parameterIndex, String x) {
        try {
            preparedStatement.setString(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setBytes(int parameterIndex, byte[] x) {
        try {
            preparedStatement.setBytes(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setDate(int parameterIndex, Date x) {
        try {
            preparedStatement.setDate(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setTime(int parameterIndex, Time x) {
        try {
            preparedStatement.setTime(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setTimestamp(int parameterIndex, Timestamp x) {
        try {
            preparedStatement.setTimestamp(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setObject(int parameterIndex, Object x) {
        try {
            preparedStatement.setObject(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Statement setObjects(int parameterIndex, Object[] x) {
        try {
            preparedStatement.setObject(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setBlob(int parameterIndex, Blob x) {
        try {
            preparedStatement.setBlob(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setClob(int parameterIndex, Clob x) {
        try {
            preparedStatement.setClob(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Statement setURL(int parameterIndex, URL x) {
        try {
            preparedStatement.setURL(parameterIndex, x);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Statement clearParameters() {
        try {
            preparedStatement.clearParameters();
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Statement push() {
        try {
            preparedStatement.execute();
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public SqlResults query() {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            db.getConnection().commit();
            return SqlResultsImpl.cache(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            JdbcUtils.closeStatement(preparedStatement);
        }
    }

    public Db commit() {
        try {
            preparedStatement.execute();
            db.getConnection().commit();
            return db;
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            JdbcUtils.closeStatement(preparedStatement);
        }
    }

    public Db rollback() {
        try {
            db.getConnection().rollback();
        } catch (SQLException ignored) {
        } finally {
            JdbcUtils.closeStatement(preparedStatement);
        }
        return db;
    }

}
