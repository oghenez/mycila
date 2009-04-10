package com.mycila.log.jdk;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public final class ClassFormatter extends Formatter {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    public String format(LogRecord record) {
        StringBuffer sb = new StringBuffer()
                .append(dateFormat.format(new Date(record.getMillis())))
                .append(" ")
                .append(record.getLevel())
                .append(" [").append(record.getSourceMethodName()).append("]")
                .append(" [").append(stripped(record.getSourceClassName())).append("]")
                .append(" - ").append(record.getMessage());
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
}
