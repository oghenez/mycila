/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.jdbc.query;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;

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
            if (x == null)
                setNull(parameterIndex);
            else
                getStatement().setBigDecimal(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setString(int parameterIndex, String x) {
        try {
            if (x == null)
                setNull(parameterIndex);
            else
                getStatement().setString(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setBytes(int parameterIndex, byte[] x) {
        try {
            if (x == null)
                setNull(parameterIndex);
            else
                getStatement().setBytes(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setDate(int parameterIndex, Date x) {
        try {
            if (x == null)
                setNull(parameterIndex);
            else
                getStatement().setDate(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setTime(int parameterIndex, Time x) {
        try {
            if (x == null)
                setNull(parameterIndex);
            else
                getStatement().setTime(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setTimestamp(int parameterIndex, Timestamp x) {
        try {
            if (x == null)
                setNull(parameterIndex);
            else
                getStatement().setTimestamp(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setObject(int parameterIndex, Object x) {
        try {
            if (x == null)
                setNull(parameterIndex);
            else
                getStatement().setObject(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public T setObjects(int parameterIndex, Object[] x) {
        try {
            if (x == null)
                setNull(parameterIndex);
            else
                getStatement().setObject(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setBlob(int parameterIndex, Blob x) {
        try {
            if (x == null)
                setNull(parameterIndex);
            else
                getStatement().setBlob(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setClob(int parameterIndex, Clob x) {
        try {
            if (x == null)
                setNull(parameterIndex);
            else
                getStatement().setClob(parameterIndex, x);
            return (T) this;
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public final T setURL(int parameterIndex, URL x) {
        try {
            if (x == null)
                setNull(parameterIndex);
            else
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

    public <E> T convert(Class<E> type, Converter<E> converter) {
        mapper.customConverters.put(type, converter);
        return (T) this;
    }

    abstract PreparedStatement getStatement();
}
