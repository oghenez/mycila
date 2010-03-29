package com.mycila.jmx.export;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultJmxMetadataBuilder implements JmxMetadataReader {
    @Override
    public JmxMetadata readJmxMetadata(Class<?> clazz) {
        DefaultJmxMetadata jmxMetadata = new DefaultJmxMetadata(clazz);
        //jmxMetadata.addField("");
        
        return jmxMetadata;
    }
}
