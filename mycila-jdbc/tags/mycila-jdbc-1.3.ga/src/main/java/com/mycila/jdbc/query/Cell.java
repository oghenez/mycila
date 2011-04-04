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
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public final class Cell {

    public final Row row;
    public final Column column;
    public final Object data;
    private final Mapper mapper;

    Cell(Row row, Column column, ResultSet resultSet, Mapper mapper) {
        this.row = row;
        this.column = column;
        this.mapper = mapper;
        try {
            this.data = JdbcUtils.getResultSetValue(resultSet, column.header.index + 1, column.header.type());
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        }
    }

    public boolean isNull() {
        return data == null;
    }

    public URL asURL() {
        return as(URL.class);
    }

    public Blob asBlob() {
        return as(Blob.class);
    }

    public Clob asClob() {
        return as(Clob.class);
    }

    public Boolean asBoolean() {
        return as(Boolean.class);
    }

    public Byte asByte() {
        return as(Byte.class);
    }

    public Short asShort() {
        return as(Short.class);
    }

    public Integer asInt() {
        return as(Integer.class);
    }

    public Long asLong() {
        return as(Long.class);
    }

    public Float asFloat() {
        return as(Float.class);
    }

    public Double asDouble() {
        return as(Double.class);
    }

    public BigDecimal asBigDecimal() {
        return as(BigDecimal.class);
    }

    public Object[] asArray() {
        return as(Object[].class);
    }

    public byte[] asBytes() {
        return as(byte[].class);
    }

    public String asString() {
        return as(String.class);
    }

    public Date asDate() {
        return as(Date.class);
    }

    public Time asTime() {
        return as(Time.class);
    }

    public Timestamp asTimestamp() {
        return as(Timestamp.class);
    }

    @SuppressWarnings({"unchecked"})
    public <T extends Enum<T>> T asEnum(Class<T> type) {
        return as(type);
    }

    public <T> T as(Class<T> type) {
        if (isNull()) {
            return null;
        } else if (ClassUtils.isAssignableValue(type, data)) {
            return type.cast(data);
        } else {
            return mapper.convert(this, type, data);
        }
    }

    @Override
    public String toString() {
        if (byte[].class.isInstance(data)) {
            return JdbcUtils.toHexString(asBytes());
        } else if (Blob.class.isInstance(data)) {
            return JdbcUtils.toHexString(JdbcUtils.readBlob((Blob) data));
        }
        return asString();
    }
}
