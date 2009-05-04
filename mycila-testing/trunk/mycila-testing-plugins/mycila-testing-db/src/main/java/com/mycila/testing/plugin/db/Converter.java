package com.mycila.testing.plugin.db;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
interface Converter<T> {
    T convert(Class<T> type, Object object);
}
