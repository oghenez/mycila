package com.mycila.testing.core.api;

import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Attributes {

    /**
     * Get an attribute from this context
     *
     * @param name Attribute name (should be unique amongst all attribute names)
     * @param <T>  Infered type of the attribute
     * @return The attribute value
     * @throws TestPluginException If the attribute does not exist (and thus we cannot get its value)
     */
    <T> T get(String name) throws TestPluginException;

    /**
     * Set an attribute
     *
     * @param name  Attribute name (should be unique amongst all attribute names)
     * @param value Attribute Value
     */
    void set(String name, Object value);

    /**
     * Check if the context has a specific attribute
     *
     * @param name Attribute name
     * @return True if this attribute exists, false otherwise
     */
    boolean has(String name);

    /**
     * Removes an attribute. Does nothing if attribute does not exist.
     *
     * @param name Attribute name
     * @param <T>  Inferred attribute type
     * @return The removed attribute or null if none has been removed
     */
    <T> T remove(String name);

    /**
     * @return All attributes from this context
     */
    Map<String, Object> all();

}
