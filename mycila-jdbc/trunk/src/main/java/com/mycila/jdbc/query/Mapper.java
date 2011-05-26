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

import com.mycila.jdbc.MycilaJdbcException;
import org.objenesis.ObjenesisStd;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

final class Mapper {

    private final ObjenesisStd objenesis = new ObjenesisStd(true);
    final Map<Class<?>, Converter<?>> customConverters = new LinkedHashMap<Class<?>, Converter<?>>();
    private final Map<Class<?>, Converter<?>> converters = new LinkedHashMap<Class<?>, Converter<?>>() {
        {
            put(URL.class, Mapper.<URL>unconvertible());
            put(Blob.class, Mapper.<Blob>unconvertible());
            put(Clob.class, Mapper.<Clob>unconvertible());
            put(Boolean.class, Mapper.BOOL_CONVERTER);
            put(Byte.class, Mapper.BYTE_CONVERTER);
            put(Short.class, Mapper.SHORT_CONVERTER);
            put(Integer.class, Mapper.INTEGER_CONVERTER);
            put(Long.class, Mapper.LONG_CONVERTER);
            put(Float.class, Mapper.FLOAT_CONVERTER);
            put(Double.class, Mapper.DOUBLE_CONVERTER);
            put(BigDecimal.class, Mapper.BIGDECIMAL_CONVERTER);
            put(Object[].class, Mapper.<Object[]>unconvertible());
            put(byte[].class, Mapper.BYTES_CONVERTER);
            put(String.class, Mapper.STRING_CONVERTER);
            put(Date.class, Mapper.DATE_CONVERTER);
            put(Time.class, Mapper.TIME_CONVERTER);
            put(Timestamp.class, Mapper.<Timestamp>unconvertible());
            put(Enum.class, Mapper.ENUM_CONVERTER);
        }
    };

    <T> T convert(Cell cell, Class<T> type, Object o) {
        if (o == null)
            return null;
        return converter(type).convert(cell, type, o);
    }

    <T> T instanciate(Class<T> type) {
        try {
            Constructor<T> ctor = type.getDeclaredConstructor();
            if (!ctor.isAccessible())
                ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (NoSuchMethodException e) {
            // ignore - try objenesis
        } catch (InstantiationException e) {
            throw new MycilaJdbcException("Unable to instanciate " + type + " using reflection: " + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new MycilaJdbcException("Unable to instanciate " + type + " using reflection: " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            Throwable ee = e.getCause();
            if (ee instanceof RuntimeException)
                throw (RuntimeException) ee;
            if (ee instanceof Error)
                throw (Error) ee;
            if (ee == null)
                throw new MycilaJdbcException("Unable to instanciate " + type + " using reflection: " + e.getMessage(), e);
            throw new MycilaJdbcException("Unable to instanciate " + type + " using reflection: " + e.getCause().getMessage(), e.getCause());
        }
        try {
            return type.cast(objenesis.newInstance(type));
        } catch (RuntimeException e) {
            throw new MycilaJdbcException("Unable to instanciate " + type + " using Objenesis: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private <T> Converter<T> converter(Class<T> type) {
        // try exact class first
        Converter<?> c = customConverters.get(type);
        if (c != null) return (Converter<T>) c;
        c = converters.get(type);
        if (c != null) return (Converter<T>) c;
        // try matching
        for (Map.Entry<Class<?>, Converter<?>> entry : customConverters.entrySet())
            if (entry.getKey().isAssignableFrom(type))
                return (Converter<T>) entry.getValue();
        for (Map.Entry<Class<?>, Converter<?>> entry : converters.entrySet())
            if (entry.getKey().isAssignableFrom(type))
                return (Converter<T>) entry.getValue();
        return (Converter<T>) NOT_CONVERTIBLE;
    }

    @SuppressWarnings({"unchecked"})
    private static <T> Converter<T> unconvertible() {
        return (Converter<T>) NOT_CONVERTIBLE;
    }

    private static final Converter<Object> NOT_CONVERTIBLE = new Converter<Object>() {
        public Object convert(Cell cell, Class<Object> type, Object object) {
            throw new ConverterException("Cannot convert object of type " + object.getClass().getName() + " to type " + type.getName());
        }
    };

    private static final Converter<Byte> BYTE_CONVERTER = new Converter<Byte>() {
        public Byte convert(Cell cell, Class<Byte> type, Object object) {
            return toNumber(object).byteValue();
        }
    };

    private static final Converter<Double> DOUBLE_CONVERTER = new Converter<Double>() {
        public Double convert(Cell cell, Class<Double> type, Object object) {
            return toNumber(object).doubleValue();
        }
    };

    private static final Converter<Short> SHORT_CONVERTER = new Converter<Short>() {
        public Short convert(Cell cell, Class<Short> type, Object object) {
            return toNumber(object).shortValue();
        }
    };

    private static final Converter<Integer> INTEGER_CONVERTER = new Converter<Integer>() {
        public Integer convert(Cell cell, Class<Integer> type, Object object) {
            return toNumber(object).intValue();
        }
    };

    private static final Converter<Long> LONG_CONVERTER = new Converter<Long>() {
        public Long convert(Cell cell, Class<Long> type, Object object) {
            return toNumber(object).longValue();
        }
    };

    private static final Converter<Float> FLOAT_CONVERTER = new Converter<Float>() {
        public Float convert(Cell cell, Class<Float> type, Object object) {
            return toNumber(object).floatValue();
        }
    };

    private static final Converter<BigDecimal> BIGDECIMAL_CONVERTER = new Converter<BigDecimal>() {
        public BigDecimal convert(Cell cell, Class<BigDecimal> type, Object object) {
            return Float.class.equals(object.getClass()) || Double.class.equals(object.getClass()) ?
                BigDecimal.valueOf(toNumber(object).doubleValue()) :
                BigDecimal.valueOf(toNumber(object).longValue());
        }
    };

    private static final Converter<byte[]> BYTES_CONVERTER = new Converter<byte[]>() {
        public byte[] convert(Cell cell, Class<byte[]> type, Object object) {
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
                return Mapper.<byte[]>unconvertible().convert(cell, type, object);
            }
        }
    };

    private static final Converter<String> STRING_CONVERTER = new Converter<String>() {
        public String convert(Cell cell, Class<String> type, Object object) {
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

    private static final Converter<Boolean> BOOL_CONVERTER = new Converter<Boolean>() {
        public Boolean convert(Cell cell, Class<Boolean> type, Object object) {
            return Boolean.valueOf(String.valueOf(object));
        }
    };

    private static final Converter<Date> DATE_CONVERTER = new Converter<Date>() {
        public Date convert(Cell cell, Class<Date> type, Object object) {
            if (object instanceof Timestamp) {
                return new Date(((Timestamp) object).getTime());
            } else {
                return Mapper.<Date>unconvertible().convert(cell, type, object);
            }
        }
    };

    private static final Converter<Time> TIME_CONVERTER = new Converter<Time>() {
        public Time convert(Cell cell, Class<Time> type, Object object) {
            if (object instanceof Timestamp) {
                return new Time(((Timestamp) object).getTime());
            } else {
                return Mapper.<Time>unconvertible().convert(cell, type, object);
            }
        }
    };

    private static final Converter<Enum> ENUM_CONVERTER = new Converter<Enum>() {
        public Enum convert(Cell cell, Class<Enum> type, Object object) {
            return Enum.valueOf(type, String.valueOf(object));
        }
    };

    private static Number toNumber(Object object) {
        if (ClassUtils.isNumber(object))
            return (Number) object;
        throw new IllegalArgumentException("Cannot convert object of type " + object.getClass().getName() + " to a Number");
    }

}
