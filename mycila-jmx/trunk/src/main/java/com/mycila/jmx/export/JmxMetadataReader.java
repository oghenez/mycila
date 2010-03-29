package com.mycila.jmx.export;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JmxMetadataReader {
    JmxMetadata readJmxMetadata(Class<?> clazz);
}
