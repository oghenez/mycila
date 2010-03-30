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

import com.mycila.jmx.export.annotation.JmxBean;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultJmxNamingStrategy implements JmxNamingStrategy {
    @Override
    public ObjectName getObjectName(Object managedBean) throws MalformedObjectNameException {
        // check JmxSelfNaming
        if (managedBean instanceof JmxSelfNaming)
            return ((JmxSelfNaming) managedBean).getObjectName();
        // check annotation
        Class<?> managedClass = AopUtils.getTargetClass(managedBean);
        JmxBean jmxBean = managedClass.getAnnotation(JmxBean.class);
        if (StringUtils.hasLength(jmxBean.objectName()))
            return ObjectName.getInstance(jmxBean.objectName());
        if (StringUtils.hasLength(jmxBean.value()))
            return ObjectName.getInstance(jmxBean.value());
        // default
        return ObjectName.getInstance(ClassUtils.getPackageName(managedClass) + ":type=" + ClassUtils.getQualifiedName(managedClass));
    }
}
