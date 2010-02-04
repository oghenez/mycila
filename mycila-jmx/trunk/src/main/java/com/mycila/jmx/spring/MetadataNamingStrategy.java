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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Hashtable;

/**
 * An implementation of the {@link com.mycila.jmx.spring.export.naming.ObjectNamingStrategy} interface
 * that reads the <code>ObjectName</code> from the source-level metadata.
 * Falls back to the bean key (bean name) if no <code>ObjectName</code>
 * can be found in source-level metadata.
 * <p/>
 * <p>Uses the {@link com.mycila.jmx.spring.export.metadata.JmxAttributeSource} strategy interface, so that
 * metadata can be read using any supported implementation. Out of the box,
 * introspects a well-defined set of Java 5 annotations that come with Spring.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @see com.mycila.jmx.spring.export.naming.ObjectNamingStrategy
 * @since 1.2
 */
public class MetadataNamingStrategy implements ObjectNamingStrategy {

    /**
     * The <code>JmxAttributeSource</code> implementation to use for reading metadata.
     */
    private JmxAttributeSource attributeSource;

    private String defaultDomain;


    /**
     * Create a new <code>MetadataNamingStrategy<code> which needs to be
     * configured through the {@link #setAttributeSource} method.
     */
    public MetadataNamingStrategy() {
    }

    /**
     * Create a new <code>MetadataNamingStrategy<code> for the given
     * <code>JmxAttributeSource</code>.
     *
     * @param attributeSource the JmxAttributeSource to use
     */
    public MetadataNamingStrategy(JmxAttributeSource attributeSource) {
        this.attributeSource = attributeSource;
    }


    /**
     * Set the implementation of the <code>JmxAttributeSource</code> interface to use
     * when reading the source-level metadata.
     */
    public void setAttributeSource(JmxAttributeSource attributeSource) {
        this.attributeSource = attributeSource;
    }

    /**
     * Specify the default domain to be used for generating ObjectNames
     * when no source-level metadata has been specified.
     * <p>The default is to use the domain specified in the bean name
     * (if the bean name follows the JMX ObjectName syntax); else,
     * the package name of the managed bean class.
     */
    public void setDefaultDomain(String defaultDomain) {
        this.defaultDomain = defaultDomain;
    }

    public void afterPropertiesSet() {
        if (this.attributeSource == null) {
            throw new IllegalArgumentException("Property 'attributeSource' is required");
        }
    }


    /**
     * Reads the <code>ObjectName</code> from the source-level metadata associated
     * with the managed resource's <code>Class</code>.
     */
    public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {
        Class managedClass = AopUtils.getTargetClass(managedBean);
        ManagedResource mr = this.attributeSource.getManagedResource(managedClass);

        // Check that an object name has been specified.
        if (mr != null && StringUtils.hasText(mr.getObjectName())) {
            return ObjectNameManager.getInstance(mr.getObjectName());
        } else {
            try {
                return ObjectNameManager.getInstance(beanKey);
            }
            catch (MalformedObjectNameException ex) {
                String domain = this.defaultDomain;
                if (domain == null) {
                    domain = ClassUtils.getPackageName(managedClass);
                }
                Hashtable<String, String> properties = new Hashtable<String, String>();
                properties.put("type", ClassUtils.getShortName(managedClass));
                properties.put("name", beanKey);
                return ObjectNameManager.getInstance(domain, properties);
            }
        }
    }

}