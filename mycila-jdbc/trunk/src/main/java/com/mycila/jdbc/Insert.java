package com.mycila.jdbc;

import net.playtouch.jaxspot.repository.sql.SqlException;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Insert {

    private final Mapper mapper = new Mapper();
    private final Request request = new Request() {
        @Override
        PreparedStatement getStatement() {
            return Insert.this.statement;
        }
    };
    private PreparedStatement statement;
    private final String table;
    private Connection connection;
    private final Map<String, Param> inserts = new LinkedHashMap<String, Param>();

    Insert(Connection connection, String table) {
        this.table = table;
        this.connection = connection;
    }

    public int execute() throws SqlException {
        try {
            buildStatement();
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        } finally {
            JdbcUtils.closeStatement(statement);
            connection = null;
        }
    }

    public Results executeAndReturns(String... columnNames) throws SqlException {
        try {
            buildStatement(columnNames);
            statement.executeUpdate();
            return Results.build(statement.getGeneratedKeys(), mapper);
        } catch (SQLException e) {
            throw new SqlException(e.getMessage(), e);
        } finally {
            JdbcUtils.closeStatement(statement);
            connection = null;
        }
    }

    void buildStatement(String... columnNames) throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO ").append(table).append(" (");
        for (String column : inserts.keySet()) {
            sb.append(column).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES (?");
        for (int i = 1; i < inserts.size(); i++)
            sb.append(",?");
        sb.append(")");
        statement = connection.prepareStatement(sb.toString(), columnNames);
        int i = 1;
        for (Param param : inserts.values())
            param.set(i++);
    }

    public final Insert setNull(String column) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setNull(index);
            }
        });
        return this;
    }

    public final Insert setBoolean(String column, final boolean x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setBoolean(index, x);
            }
        });
        return this;
    }

    public final Insert setByte(String column, final byte x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setByte(index, x);
            }
        });
        return this;
    }

    public final Insert setShort(String column, final short x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setShort(index, x);
            }
        });
        return this;
    }

    public final Insert setInt(String column, final int x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setInt(index, x);
            }
        });
        return this;
    }

    public final Insert setLong(String column, final long x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setLong(index, x);
            }
        });
        return this;
    }

    public final Insert setFloat(String column, final float x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setFloat(index, x);
            }
        });
        return this;
    }

    public final Insert setDouble(String column, final double x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setDouble(index, x);
            }
        });
        return this;
    }

    public final Insert setBigDecimal(String column, final BigDecimal x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setBigDecimal(index, x);
            }
        });
        return this;
    }

    public final Insert setString(String column, final String x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setString(index, x);
            }
        });
        return this;
    }

    public final Insert setBytes(String column, final byte[] x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setBytes(index, x);
            }
        });
        return this;
    }

    public final Insert setDate(String column, final Date x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setDate(index, x);
            }
        });
        return this;
    }

    public final Insert setTime(String column, final Time x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setTime(index, x);
            }
        });
        return this;
    }

    public final Insert setTimestamp(String column, final Timestamp x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setTimestamp(index, x);
            }
        });
        return this;
    }

    public final Insert setObject(String column, final Object x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setObject(index, x);
            }
        });
        return this;
    }

    public Insert setObjects(String column, final Object[] x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setObjects(index, x);
            }
        });
        return this;
    }

    public final Insert setBlob(String column, final Blob x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setBlob(index, x);
            }
        });
        return this;
    }

    public final Insert setClob(String column, final Clob x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setClob(index, x);
            }
        });
        return this;
    }

    public final Insert setURL(String column, final URL x) {
        inserts.put(column, new Param() {
            @Override
            public void set(int index) {
                request.setURL(index, x);
            }
        });
        return this;
    }

    private static interface Param {
        void set(int index);
    }

}
