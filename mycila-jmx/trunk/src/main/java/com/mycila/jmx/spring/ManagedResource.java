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

package com.mycila.jmx.spring;

/**
 * Metadata indicating that instances of an annotated class
 * are to be registered with a JMX server.
 * Only valid when used on a <code>Class</code>.
 *
 * @author Rob Harrop
 * @see com.mycila.jmx.spring.export.assembler.MetadataMBeanInfoAssembler
 * @see com.mycila.jmx.spring.export.naming.MetadataNamingStrategy
 * @see com.mycila.jmx.spring.export.MBeanExporter
 * @since 1.2
 */
public class ManagedResource extends AbstractJmxAttribute {

    private String objectName;


    /**
     * Set the JMX ObjectName of this managed resource.
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    /**
     * Return the JMX ObjectName of this managed resource.
     */
    public String getObjectName() {
        return this.objectName;
    }

}