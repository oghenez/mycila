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
