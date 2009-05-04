package com.mycila.testing.plugin.db.api;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface SqlData {
    int rowIndex();

    SqlRow row();

    int columnIndex();

    SqlColumn column();

    SqlColumnHeader header();

    Object value();

    boolean isNull();

    Boolean asBoolean();

    Byte asByte();

    Short asShort();

    Integer asInt();

    Long asLong();

    Float asFloat();

    Double asDouble();

    byte[] asBytes();

    String asString();

    URL asURL();

    Blob asBlob();

    Clob asClob();

    Date asDate();

    Time asTime();

    Timestamp asTimestamp();

    BigDecimal asBigDecimal();

    Object[] asArray();
}
