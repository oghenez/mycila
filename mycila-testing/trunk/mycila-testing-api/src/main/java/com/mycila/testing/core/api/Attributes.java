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
