/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.jmx.export;

import javax.management.ObjectName;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JmxOperations {

    /**
     * Unregister an object name from the MBean server
     *
     * @param objectName The object name to unregister
     */
    void unregister(ObjectName objectName);

    /**
     * Register an object to the given object name
     *
     * @param objectName Object name to register to
     * @param o          Object to register
     * @throws JmxExporterException The registration failed
     */
    void register(ObjectName objectName, Object o) throws JmxExporterException;

    /**
     * Register an object and discover which object name to use through
     * {@link com.mycila.jmx.export.annotation.JmxBean} annotation or
     * {@link com.mycila.jmx.export.SelfNaming} interface.
     * If the object name cannot be determines, will use the form
     * <code>package:type=classname</code>.
     *
     * @param o Object to register
     * @throws JmxExporterException The registration failed
     */
    void register(Object o) throws JmxExporterException;
}
