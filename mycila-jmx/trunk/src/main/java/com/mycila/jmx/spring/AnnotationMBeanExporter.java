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

public class AnnotationMBeanExporter extends MBeanExporter {

    private final AnnotationJmxAttributeSource annotationSource =
            new AnnotationJmxAttributeSource();

    private final MetadataNamingStrategy metadataNamingStrategy =
            new MetadataNamingStrategy(this.annotationSource);

    private final MetadataMBeanInfoAssembler metadataAssembler =
            new MetadataMBeanInfoAssembler(this.annotationSource);


    public AnnotationMBeanExporter() {
        setNamingStrategy(this.metadataNamingStrategy);
        setAssembler(this.metadataAssembler);
        setAutodetectMode(AUTODETECT_ALL);
    }


    /**
     * Specify the default domain to be used for generating ObjectNames
     * when no source-level metadata has been specified.
     * <p>The default is to use the domain specified in the bean name
     * (if the bean name follows the JMX ObjectName syntax); else,
     * the package name of the managed bean class.
     */
    public void setDefaultDomain(String defaultDomain) {
        this.metadataNamingStrategy.setDefaultDomain(defaultDomain);
    }

}