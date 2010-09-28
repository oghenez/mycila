package com.mycila.jdbc;

import net.playtouch.jaxspot.repository.sql.SqlException;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

abstract class Request<T extends Request> {

    protected final Mapper mapper = new Mapper();

    public final T setNull(int parameterIndex) {
        try {
            getStatement().setNull(parameterIndex, SqlType.NULL.sqlType());
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setBoolean(int parameterIndex, boolean x) {
        try {
            getStatement().setBoolean(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setByte(int parameterIndex, byte x) {
        try {
            getStatement().setByte(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setShort(int parameterIndex, short x) {
        try {
            getStatement().setShort(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setInt(int parameterIndex, int x) {
        try {
            getStatement().setInt(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setLong(int parameterIndex, long x) {
        try {
            getStatement().setLong(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setFloat(int parameterIndex, float x) {
        try {
            getStatement().setFloat(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setDouble(int parameterIndex, double x) {
        try {
            getStatement().setDouble(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setBigDecimal(int parameterIndex, BigDecimal x) {
        try {
            getStatement().setBigDecimal(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setString(int parameterIndex, String x) {
        try {
            getStatement().setString(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setBytes(int parameterIndex, byte[] x) {
        try {
            getStatement().setBytes(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setDate(int parameterIndex, Date x) {
        try {
            getStatement().setDate(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setTime(int parameterIndex, Time x) {
        try {
            getStatement().setTime(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setTimestamp(int parameterIndex, Timestamp x) {
        try {
            getStatement().setTimestamp(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setObject(int parameterIndex, Object x) {
        try {
            getStatement().setObject(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public T setObjects(int parameterIndex, Object[] x) {
        try {
            getStatement().setObject(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setBlob(int parameterIndex, Blob x) {
        try {
            getStatement().setBlob(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setClob(int parameterIndex, Clob x) {
        try {
            getStatement().setClob(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setURL(int parameterIndex, URL x) {
        try {
            getStatement().setURL(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public T clearParameters() {
        try {
            getStatement().clearParameters();
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    abstract PreparedStatement getStatement();
}
