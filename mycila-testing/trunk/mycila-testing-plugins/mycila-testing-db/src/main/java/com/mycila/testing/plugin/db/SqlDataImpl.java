package com.mycila.testing.plugin.db;

import com.mycila.testing.plugin.db.api.SqlColumn;
import com.mycila.testing.plugin.db.api.SqlColumnHeader;
import com.mycila.testing.plugin.db.api.SqlData;
import com.mycila.testing.plugin.db.api.SqlResults;
import com.mycila.testing.plugin.db.api.SqlRow;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SqlDataImpl implements SqlData {

    final SqlResults results;
    final SqlRow sqlRow;
    final SqlColumn sqlColumn;
    final Object object;

    SqlDataImpl(SqlResults results, SqlRow sqlRow, SqlColumn sqlColumn, ResultSet resultSet) {
        this.results = results;
        this.sqlRow = sqlRow;
        this.sqlColumn = sqlColumn;
        try {
            this.object = JdbcUtils.getResultSetValue(resultSet, sqlColumn.index() + 1, sqlColumn.header().type());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public int rowIndex() {
        return sqlRow.index();
    }

    public SqlRow row() {
        return sqlRow;
    }

    public int columnIndex() {
        return sqlColumn.index();
    }

    public SqlColumn column() {
        return sqlColumn;
    }

    public SqlColumnHeader header() {
        return sqlColumn.header();
    }

    @SuppressWarnings({"unchecked"})
    public Object value() {
        return object;
    }

    public boolean isNull() {
        return object == null;
    }

    public URL asURL() {
        return as(URL.class, Converters.<URL>unconvertible());
    }

    public Blob asBlob() {
        return as(Blob.class, Converters.<Blob>unconvertible());
    }

    public Clob asClob() {
        return as(Clob.class, Converters.<Clob>unconvertible());
    }

    public Boolean asBoolean() {
        return as(Boolean.class, Converters.BOOL_CONVERTER);
    }

    public Byte asByte() {
        return as(Byte.class, Converters.BYTE_CONVERTER);
    }

    public Short asShort() {
        return as(Short.class, Converters.SHORT_CONVERTER);
    }

    public Integer asInt() {
        return as(Integer.class, Converters.INTEGER_CONVERTER);
    }

    public Long asLong() {
        return as(Long.class, Converters.LONG_CONVERTER);
    }

    public Float asFloat() {
        return as(Float.class, Converters.FLOAT_CONVERTER);
    }

    public Double asDouble() {
        return as(Double.class, Converters.DOUBLE_CONVERTER);
    }

    public BigDecimal asBigDecimal() {
        return as(BigDecimal.class, Converters.BIGDECIMAL_CONVERTER);
    }

    public Object[] asArray() {
        return as(Object[].class, Converters.<Object[]>unconvertible());
    }

    public byte[] asBytes() {
        return as(byte[].class, Converters.BYTES_CONVERTER);
    }

    public String asString() {
        return as(String.class, Converters.STRING_CONVERTER);
    }

    public Date asDate() {
        return as(Date.class, Converters.DATE_CONVERTER);
    }

    public Time asTime() {
        return as(Time.class, Converters.TIME_CONVERTER);
    }

    public Timestamp asTimestamp() {
        return as(Timestamp.class, Converters.<Timestamp>unconvertible());
    }

    @SuppressWarnings({"unchecked"})
    <T> T as(Class<T> type, Converter<T> converter) {
        if (isNull()) {
            return null;
        } else if (ClassUtils.isAssignableValue(type, object)) {
            return (T) object;
        } else {
            return converter.convert(type, object);
        }
    }

    @Override
    public String toString() {
        if (byte[].class.isInstance(object)) {
            return JdbcUtils.toHexString(asBytes());
        } else if (Blob.class.isInstance(object)) {
            return JdbcUtils.toHexString(JdbcUtils.readBlob((Blob) object));
        }
        return asString();
    }
}
