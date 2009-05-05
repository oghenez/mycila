package com.mycila.testing.plugin.db.api;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Statement {

    Statement setNull(int parameterIndex);

    Statement setBoolean(int parameterIndex, boolean x);

    Statement setByte(int parameterIndex, byte x);

    Statement setShort(int parameterIndex, short x);

    Statement setInt(int parameterIndex, int x);

    Statement setLong(int parameterIndex, long x);

    Statement setFloat(int parameterIndex, float x);

    Statement setDouble(int parameterIndex, double x);

    Statement setBigDecimal(int parameterIndex, BigDecimal x);

    Statement setString(int parameterIndex, String x);

    Statement setBytes(int parameterIndex, byte x[]);

    Statement setDate(int parameterIndex, Date x);

    Statement setTime(int parameterIndex, Time x);

    Statement setTimestamp(int parameterIndex, Timestamp x);

    Statement setObject(int parameterIndex, Object x);

    Statement setObjects(int parameterIndex, Object[] x);

    Statement setBlob(int i, Blob x);

    Statement setClob(int i, Clob x);

    Statement setURL(int parameterIndex, java.net.URL x);

    /**
     * Clear parameters previously set
     *
     * @return this
     */
    Statement clearParameters();

    /**
     * Push current statement for execution and give ability to execute a new one
     *
     * @return this
     */
    Statement push();

    /**
     * Run the query and commit the statement
     *
     * @return the results of the query
     */
    SqlResults query();

    /**
     * Commit all statements
     *
     * @return Db object we were previously
     */
    Db commit();

    /**
     * Cancel all statements
     *
     * @return Db object we were previously
     */
    Db rollback();
}
