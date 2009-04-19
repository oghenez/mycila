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
package com.mycila.testing.core;

import com.mycila.testing.core.api.Attributes;
import static com.mycila.testing.core.api.Ensure.*;
import com.mycila.testing.core.api.TestPluginException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class AttributesImpl implements Attributes {

    private final Map<String, Object> attributes = new HashMap<String, Object>();

    @SuppressWarnings({"unchecked"})
    public <T> T get(String name) {
        notNull("Attribute name", name);
        T att = (T) attributes.get(name);
        if (att == null) {
            throw new TestPluginException("Inexisting attribute: '%s'", name);
        }
        return att;
    }

    public boolean has(String name) {
        notNull("Attribute name", name);
        return attributes.containsKey(name);
    }

    public void set(String name, Object value) {
        notNull("Attribute name", name);
        attributes.put(name, value);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T remove(String name) {
        notNull("Attribute name", name);
        return (T) attributes.remove(name);
    }

    public Map<String, Object> all() {
        return Collections.unmodifiableMap(attributes);
    }

}
