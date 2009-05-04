package com.mycila.testing.plugin.db;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Converters {

    private Converters() {
    }


    @SuppressWarnings({"unchecked"})
    static <T> Converter<T> unconvertible() {
        return (Converter<T>) NOT_CONVERTIBLE;
    }

    static final Converter<Object> NOT_CONVERTIBLE = new Converter<Object>() {
        public Object convert(Class<Object> type, Object object) {
            throw new IllegalArgumentException("Cannot convert object of type " + object.getClass().getName() + " to type " + type.getName());
        }
    };

    static final Converter<Byte> BYTE_CONVERTER = new Converter<Byte>() {
        public Byte convert(Class<Byte> type, Object object) {
            return toNumber(object).byteValue();
        }
    };

    static final Converter<Double> DOUBLE_CONVERTER = new Converter<Double>() {
        public Double convert(Class<Double> type, Object object) {
            return toNumber(object).doubleValue();
        }
    };

    static final Converter<Short> SHORT_CONVERTER = new Converter<Short>() {
        public Short convert(Class<Short> type, Object object) {
            return toNumber(object).shortValue();
        }
    };

    static final Converter<Integer> INTEGER_CONVERTER = new Converter<Integer>() {
        public Integer convert(Class<Integer> type, Object object) {
            return toNumber(object).intValue();
        }
    };

    static final Converter<Long> LONG_CONVERTER = new Converter<Long>() {
        public Long convert(Class<Long> type, Object object) {
            return toNumber(object).longValue();
        }
    };

    static final Converter<Float> FLOAT_CONVERTER = new Converter<Float>() {
        public Float convert(Class<Float> type, Object object) {
            return toNumber(object).floatValue();
        }
    };

    static final Converter<BigDecimal> BIGDECIMAL_CONVERTER = new Converter<BigDecimal>() {
        public BigDecimal convert(Class<BigDecimal> type, Object object) {
            return Float.class.equals(object.getClass()) || Double.class.equals(object.getClass()) ?
                    BigDecimal.valueOf(toNumber(object).doubleValue()) :
                    BigDecimal.valueOf(toNumber(object).longValue());
        }
    };

    static final Converter<byte[]> BYTES_CONVERTER = new Converter<byte[]>() {
        public byte[] convert(Class<byte[]> type, Object object) {
            if (object instanceof InputStream) {
                return JdbcUtils.readFully((InputStream) object);
            } else if (object instanceof Reader) {
                return JdbcUtils.readFully((Reader) object).getBytes();
            } else if (object instanceof String) {
                return ((String) object).getBytes();
            } else if (object instanceof Blob) {
                return JdbcUtils.readBlob((Blob) object);
            } else if (object instanceof Clob) {
                return JdbcUtils.readClob((Clob) object).getBytes();
            } else {
                return Converters.<byte[]>unconvertible().convert(type, object);
            }
        }
    };

    static final Converter<String> STRING_CONVERTER = new Converter<String>() {
        public String convert(Class<String> type, Object object) {
            if (object instanceof InputStream) {
                return new String(JdbcUtils.readFully((InputStream) object));
            } else if (object instanceof Reader) {
                return JdbcUtils.readFully((Reader) object);
            } else if (object instanceof Blob) {
                return new String(JdbcUtils.readBlob((Blob) object));
            } else if (object instanceof Clob) {
                return JdbcUtils.readClob((Clob) object);
            } else if (byte[].class.isInstance(object)) {
                return new String((byte[]) object);
            } else if (Object[].class.isInstance(object)) {
                return Arrays.toString((Object[]) object);
            } else {
                return String.valueOf(object);
            }
        }
    };

    static final Converter<Boolean> BOOL_CONVERTER = new Converter<Boolean>() {
        public Boolean convert(Class<Boolean> type, Object object) {
            return Boolean.valueOf(String.valueOf(object));
        }
    };

    static final Converter<Date> DATE_CONVERTER = new Converter<Date>() {
        public Date convert(Class<Date> type, Object object) {
            if (object instanceof Timestamp) {
                return new Date(((Timestamp) object).getTime());
            } else {
                return Converters.<Date>unconvertible().convert(type, object);
            }
        }
    };

    static final Converter<Time> TIME_CONVERTER = new Converter<Time>() {
        public Time convert(Class<Time> type, Object object) {
            if (object instanceof Timestamp) {
                return new Time(((Timestamp) object).getTime());
            } else {
                return Converters.<Time>unconvertible().convert(type, object);
            }
        }
    };

    private static Number toNumber(Object object) {
        if (ClassUtils.isNumber(object)) {
            return (Number) object;

        }
        throw new IllegalArgumentException("Cannot convert object of type " + object.getClass().getName() + " to a Number");
    }

}
