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
package com.mycila.log.jdk.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public final class ClassFormatter extends Formatter {

    private DateFormat dateFormat;
    private String pattern;

    public ClassFormatter() {
        setDatePattern("yyyy-MM-dd HH:mm:ss,SSS");
    }

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    public String format(LogRecord record) {
        String src = record.getSourceClassName();
        if(src == null) src = notNull(record.getLoggerName());
        StringBuffer sb = new StringBuffer()
                .append(dateFormat.format(new Date(record.getMillis())))
                .append(" ")
                .append(record.getLevel())
                .append(" [").append(notNull(record.getSourceMethodName())).append("]")
                .append(" [").append(stripped(notNull(src))).append("]")
                .append(" - ").append(record.getMessage())
                .append(Utils.EOL);
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ignored) {
            }
        }
        return sb.toString();
    }

    String stripped(String name) {
        int pos = name.length();
        int count = 2;
        //noinspection StatementWithEmptyBody
        while ((pos = name.lastIndexOf(".", pos - 1)) != -1 && count-- > 0) ;
        pos++;
        if (pos < name.length() && name.charAt(pos) == '.') pos++;
        return name.substring(pos);
    }

    private String notNull(String s) {
        return s == null ? "?" : s;
    }

    public void setDatePattern(String pattern) {
        this.pattern = pattern;
        dateFormat = new SimpleDateFormat(pattern);
    }

    public String getDatePattern() {
        return pattern;
    }
}
